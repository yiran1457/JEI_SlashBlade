package net.yiran.jsb.recipe.manager;

import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.advanced.ISimpleRecipeManagerPlugin;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;

import java.util.List;

@SuppressWarnings("all")
public class SARecipeManager implements ISimpleRecipeManagerPlugin<SlashArts> {
    public static SARecipeManager INSTANCE = new SARecipeManager();
    @Override
    public boolean isHandledInput(ITypedIngredient<?> iTypedIngredient) {
        return iTypedIngredient.getItemStack().filter(stack -> stack.getCapability(ItemSlashBlade.BLADESTATE).isPresent()).isPresent();
    }

    @Override
    public boolean isHandledOutput(ITypedIngredient<?> iTypedIngredient) {
        return false;
    }

    @Override
    public List<SlashArts> getRecipesForInput(ITypedIngredient<?> iTypedIngredient) {
        return List.of(iTypedIngredient.getItemStack().get().getCapability(ItemSlashBlade.BLADESTATE).orElse(null).getSlashArts());
    }

    @Override
    public List<SlashArts> getRecipesForOutput(ITypedIngredient<?> iTypedIngredient) {
        return List.of();
    }

    @Override
    public List<SlashArts> getAllRecipes() {
        return SlashArtsRegistry.REGISTRY.get().getValues().stream().toList();
    }
}
