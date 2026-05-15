package net.yiran.jsb.recipe.category;

import net.yiran.jsb.Config;
import net.yiran.jsb.JEISlashBlade;
import net.yiran.jsb.ingredient.SAIngredient;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.gui.elements.DrawableText;
import mods.flammpfeil.slashblade.SlashBladeCreativeGroup;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@SuppressWarnings("all")
public class SARecipeCategory implements IRecipeCategory<SlashArts> {
    public static SARecipeCategory INSTANCE = new SARecipeCategory();
    public static RecipeType<SlashArts> SA_TYPE = RecipeType.create(JEISlashBlade.MODID, "sa", SlashArts.class);

    @Override
    public RecipeType<SlashArts> getRecipeType() {
        return SA_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("SA");
    }

    @Override
    public int getWidth() {
        return Config.SA_RECIPE_WIDTH.get();
    }

    @Override
    public int getHeight() {
        return Config.SA_RECIPE_HEIGHT.get();
    }

    @Override
    public IDrawable getIcon() {
        return new DrawableText("SA", 16, 16, -1);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SlashArts slashArts, IFocusGroup iFocusGroup) {
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addIngredient(SAIngredient.INSTANCE, slashArts);
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                .addIngredient(SAIngredient.INSTANCE, slashArts);
        List<ItemStack> items = SlashBladeCreativeGroup.SLASHBLADE_GROUP.get().getDisplayItems()
                .stream()
                .filter(stack -> stack.getItem() instanceof ItemSlashBlade)
                .filter(stack -> stack.getCapability(ItemSlashBlade.BLADESTATE).map(state -> state.getSlashArts().equals(slashArts)).orElse(false))
                .toList();
        if (!items.isEmpty()) {
            builder.addInputSlot(0, 0)
                    .addItemStacks(items);
        }

    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, SlashArts recipe, IFocusGroup focuses) {
        builder.addText(recipe.getDescription(), getWidth()-20, 10)
                .setPosition(20, 5);
        builder.addScrollBoxWidget(getWidth() - 10, getHeight() - 30, 5,25 )
                .setContents(List.of(Component.translatable(recipe.getDescriptionId() + ".desc")));
    }

    @Override
    public ResourceLocation getRegistryName(SlashArts recipe) {
        return SlashArtsRegistry.REGISTRY.get().getKey(recipe);
    }
}
