package net.yiran.jsb.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.yiran.jsb.data.SpecialBladeDescData;
import net.yiran.jsb.recipe.BladeRegisterManager;

import java.util.List;

public class SlashBladeDescEmiRecipe implements EmiRecipe {
    private final SpecialBladeDescData descData;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;
    private final ItemStack blade;

    public SlashBladeDescEmiRecipe(SpecialBladeDescData descData) {
        this.descData = descData;
        this.id = ResourceLocation.parse("jei_slashblade:/desc/" + descData.bladeName().getNamespace() + "/" + descData.bladeName().getPath());

        ItemStack bladeItem = BladeRegisterManager.getBlade(descData.bladeName());
        if (bladeItem == null) {
            bladeItem = Items.BARRIER.getDefaultInstance();
        }
        this.blade = bladeItem;
        this.inputs = List.of(EmiStack.of(bladeItem));
        this.outputs = List.of(EmiStack.of(bladeItem));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return SlashBladeEmiPlugin.DESC_CATEGORY;
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
        // 拔刀槽位
        widgets.addSlot(outputs.get(0), 4, 4).recipeContext(this);

        // 彩色拔刀名称（带稀有度颜色）
        MutableComponent nameComponent = ((MutableComponent) blade.getHoverName()).withStyle(blade.getRarity().getStyleModifier());
        widgets.addText(nameComponent, 26, 8, 0xFFFFFFFF, true);

        // 滚动文本框特殊描述
        Component desc = Component.translatable(descData.description());
        widgets.add(new ScrollTextWidget(4, 26, 152, 56, desc));
    }
}
