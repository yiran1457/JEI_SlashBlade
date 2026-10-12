package net.yiran.jsb.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import mods.flammpfeil.slashblade.registry.SpecialEffectsRegistry;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.yiran.jsb.recipe.BladeRegisterManager;
import net.yiran.jsb.recipe.category.SERecipeCategory;

import java.util.List;

public class SlashBladeSEEmiRecipe implements EmiRecipe {
    private final SpecialEffect specialEffect;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public SlashBladeSEEmiRecipe(SpecialEffect specialEffect) {
        this.specialEffect = specialEffect;
        ResourceLocation seKey = SpecialEffectsRegistry.REGISTRY.get().getKey(specialEffect);
        this.id = ResourceLocation.parse("jei_slashblade:/se/" + seKey.getNamespace() + "/" + seKey.getPath());

        List<ItemStack> matchingBlades = BladeRegisterManager.getAllBlades().stream()
                .filter(stack -> stack.getCapability(ItemSlashBlade.BLADESTATE)
                        .map(state -> state.getSpecialEffects().contains(seKey))
                        .orElse(false))
                .toList();

        if (matchingBlades.isEmpty()) {
            this.inputs = List.of();
        } else {
            this.inputs = List.of(EmiIngredient.of(matchingBlades.stream().map(EmiStack::of).toList()));
        }

        ItemStack crystal = SlashBladeItems.PROUDSOUL_CRYSTAL.get().getDefaultInstance();
        this.outputs = List.of(EmiStack.of(crystal));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return SlashBladeEmiPlugin.SE_CATEGORY;
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
        return 96;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        // 拔刀槽位（输入）
        if (!inputs.isEmpty()) {
            widgets.addSlot(inputs.get(0), 4, 4);
        } else {
            widgets.addSlot(4, 4);
        }

        // 对应 SE 耀魂结晶（输出）
        widgets.addSlot(outputs.get(0), 26, 4).recipeContext(this);

        // 标题 SE 名称
        Component title = specialEffect.getDescription();
        widgets.addText(title, 48, 8, 0xFFFFFFFF, true);

        // 可复制 / 可删除属性
        Component copiable = Component.translatable("jsb.se.copiable", SERecipeCategory.getBooleanText(specialEffect.isCopiable()));
        Component removable = Component.translatable("jsb.se.removable", SERecipeCategory.getBooleanText(specialEffect.isRemovable()));
        Component flags = copiable.copy().append(Component.literal("   ")).append(removable);
        widgets.addText(flags, 4, 26, 0xFFAAAAAA, false);

        // 滚动文本框详细描述
        Component desc = Component.translatable(specialEffect.getDescriptionId() + ".desc");
        widgets.add(new ScrollTextWidget(4, 38, 152, 54, desc));
    }
}
