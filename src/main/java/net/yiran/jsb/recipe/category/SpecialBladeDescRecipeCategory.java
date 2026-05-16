package net.yiran.jsb.recipe.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.yiran.jsb.JEISlashBlade;
import net.yiran.jsb.data.SpecialBladeDescData;
import net.yiran.jsb.recipe.BladeRegisterManager;

import java.util.List;

@SuppressWarnings({"removal", "all"})
public class SpecialBladeDescRecipeCategory implements IRecipeCategory<SpecialBladeDescData> {
    public static SpecialBladeDescRecipeCategory INSTANCE;
    public static RecipeType<SpecialBladeDescData> DESC_TYPE = RecipeType.create(JEISlashBlade.MODID, "desc", SpecialBladeDescData.class);
    public IDrawable icon;

    public SpecialBladeDescRecipeCategory(IJeiHelpers jeiHelpers) {
        this.icon = jeiHelpers.getGuiHelper().createDrawableItemStack(BladeRegisterManager.getBlade(ResourceLocation.parse("slashblade:koseki")));
    }

    public static SpecialBladeDescRecipeCategory getInstance(IJeiHelpers jeiHelpers) {
        if (INSTANCE == null) {
            INSTANCE = new SpecialBladeDescRecipeCategory(jeiHelpers);
        }
        return INSTANCE;
    }

    @Override
    public RecipeType<SpecialBladeDescData> getRecipeType() {
        return DESC_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jsb.title.desc");
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
        return icon;
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
