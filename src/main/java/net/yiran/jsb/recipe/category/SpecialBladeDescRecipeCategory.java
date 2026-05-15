package net.yiran.jsb.recipe.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.gui.elements.DrawableText;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.yiran.jsb.JEISlashBlade;
import net.yiran.jsb.data.SpecialBladeDescData;
import net.yiran.jsb.recipe.BladeRegisterManager;

import java.util.List;

public class SpecialBladeDescRecipeCategory implements IRecipeCategory<SpecialBladeDescData> {
    public static SpecialBladeDescRecipeCategory INSTANCE = new SpecialBladeDescRecipeCategory();
    public static RecipeType<SpecialBladeDescData> DESC_TYPE = RecipeType.create(JEISlashBlade.MODID, "desc", SpecialBladeDescData.class);

    @Override
    public RecipeType<SpecialBladeDescData> getRecipeType() {
        return DESC_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("DESC");
    }

    @Override
    public int getWidth() {
        return 128;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public IDrawable getIcon() {
        return new DrawableText("DESC", 16, 16, -1);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SpecialBladeDescData specialBladeDescData, IFocusGroup iFocusGroup) {
        builder.addOutputSlot(5, 2)
                .setStandardSlotBackground()
                .addItemStack(BladeRegisterManager.getBlade(specialBladeDescData.bladeName()));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, SpecialBladeDescData recipe, IFocusGroup focuses) {
        var blade = BladeRegisterManager.getBlade(recipe.bladeName());
        builder.addText(((MutableComponent) blade.getHoverName()).withStyle(blade.getRarity().getStyleModifier()), getWidth() - 42, 10)
                .setTextAlignment(HorizontalAlignment.CENTER)
                .setPosition(21, 7);
        builder.addScrollBoxWidget(getWidth() - 10, getHeight() - 40, 5, 35)
                .setContents(List.of(Component.translatable(recipe.description())));
    }
}
