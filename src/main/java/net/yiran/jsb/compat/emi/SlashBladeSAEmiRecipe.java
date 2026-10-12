package net.yiran.jsb.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.yiran.jsb.recipe.BladeRegisterManager;

import java.util.List;

public class SlashBladeSAEmiRecipe implements EmiRecipe {
    private final SlashArts slashArts;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public SlashBladeSAEmiRecipe(SlashArts slashArts) {
        this.slashArts = slashArts;
        ResourceLocation saKey = SlashArtsRegistry.REGISTRY.get().getKey(slashArts);
        this.id = ResourceLocation.parse("jei_slashblade:/sa/" + saKey.getNamespace() + "/" + saKey.getPath());

        List<ItemStack> matchingBlades = BladeRegisterManager.getAllBlades().stream()
                .filter(stack -> stack.getCapability(ItemSlashBlade.BLADESTATE)
                        .map(state -> state.getSlashArts().equals(slashArts))
                        .orElse(false))
                .toList();

        if (matchingBlades.isEmpty()) {
            this.inputs = List.of();
        } else {
            this.inputs = List.of(EmiIngredient.of(matchingBlades.stream().map(EmiStack::of).toList()));
        }

        ItemStack sphere = SlashBladeItems.PROUDSOUL_SPHERE.get().getDefaultInstance();
        CompoundTag tag = new CompoundTag();
        tag.putString("SpecialAttackType", saKey.toString());
        sphere.setTag(tag);
        this.outputs = List.of(EmiStack.of(sphere));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return SlashBladeEmiPlugin.SA_CATEGORY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return 160;
    }

    @Override
    public int getDisplayHeight() {
        return 86;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        // 拔刀槽位（输入）
        if (!inputs.isEmpty()) {
            widgets.addSlot(inputs.get(0), 4, 4);
        } else {
            widgets.addSlot(4, 4);
        }

        // 对应 SA 耀魂宝珠（输出）
        widgets.addSlot(outputs.get(0), 26, 4).recipeContext(this);

        // 标题 SA 名称
        Component title = slashArts.getDescription();
        widgets.addText(title, 48, 8, 0xFFFFFFFF, true);

        // 滚动文本框详细描述
        Component desc = Component.translatable(slashArts.getDescriptionId() + ".desc");
        widgets.add(new ScrollTextWidget(4, 26, 152, 56, desc));
    }
}
