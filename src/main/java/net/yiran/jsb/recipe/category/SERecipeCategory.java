package net.yiran.jsb.recipe.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.gui.elements.DrawableText;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SpecialEffectsRegistry;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.yiran.jsb.Config;
import net.yiran.jsb.JEISlashBlade;
import net.yiran.jsb.ingredient.SEIngredient;
import net.yiran.jsb.recipe.BladeRegisterManager;

import java.util.List;

@SuppressWarnings("all")
public class SERecipeCategory implements IRecipeCategory<SpecialEffect> {
    public static SERecipeCategory INSTANCE = new SERecipeCategory();
    public static RecipeType<SpecialEffect> SE_TYPE = RecipeType.create(JEISlashBlade.MODID, "se", SpecialEffect.class);

    @Override
    public RecipeType<SpecialEffect> getRecipeType() {
        return SE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("SE");
    }

    @Override
    public int getWidth() {
        return Config.SE_RECIPE_WIDTH.get();
    }

    @Override
    public int getHeight() {
        return Config.SE_RECIPE_HEIGHT.get();
    }

    @Override
    public IDrawable getIcon() {
        return new DrawableText("SE", 16, 16, -1);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SpecialEffect specialEffect, IFocusGroup iFocusGroup) {
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addIngredient(SEIngredient.INSTANCE, specialEffect);
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                .addIngredient(SEIngredient.INSTANCE, specialEffect);
        List<ItemStack> items = BladeRegisterManager.getAllBlades().stream()
                .filter(stack -> BladeStateAccess.of(stack).map(state -> state.getSpecialEffects().contains(SpecialEffectsRegistry.REGISTRY.getKey(specialEffect))).orElse(false))
                .toList();
        if (!items.isEmpty()) {
            builder.addInputSlot(5, 2)
                    .addItemStacks(items);
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, SpecialEffect recipe, IFocusGroup focuses) {
        builder.addText(recipe.getDescription(), getWidth() - 20, 10)
                .setPosition(25, 7);
        builder.addText(
                        Component.translatable("jsb.se.copiable", getBooleanText(recipe.isCopiable()))
                                .append(Component.literal("   "))
                                .append(
                                        Component.translatable("jsb.se.removable", getBooleanText(recipe.isRemovable()))
                                ),
                        getWidth(), 10
                )
                .setTextAlignment(HorizontalAlignment.CENTER)
                .setPosition(0, 22);
        builder.addScrollBoxWidget(getWidth() - 10, getHeight() - 40, 5, 35)
                .setContents(List.of(Component.translatable(recipe.getDescriptionId() + ".desc")));
    }

    public static Component getBooleanText(boolean value) {
        return value ? Component.translatable("jsb.se.true") : Component.translatable("jsb.se.false");
    }

    @Override
    public ResourceLocation getRegistryName(SpecialEffect recipe) {
        return SpecialEffectsRegistry.REGISTRY.getKey(recipe);
    }

}
