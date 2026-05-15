package net.yiran.jsb.recipe.manager;

import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.advanced.ISimpleRecipeManagerPlugin;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SpecialEffectsRegistry;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;

import java.util.List;

@SuppressWarnings("all")
public class SERecipeManager implements ISimpleRecipeManagerPlugin<SpecialEffect> {
    public static SERecipeManager INSTANCE = new SERecipeManager();

    @Override
    public boolean isHandledInput(ITypedIngredient<?> iTypedIngredient) {
        return iTypedIngredient.getItemStack().filter(stack -> BladeStateAccess.of(stack).isPresent()).isPresent();
    }

    @Override
    public boolean isHandledOutput(ITypedIngredient<?> iTypedIngredient) {
        return false;
    }

    @Override
    public List<SpecialEffect> getRecipesForInput(ITypedIngredient<?> iTypedIngredient) {
        return BladeStateAccess.of(iTypedIngredient.getItemStack().get()).orElse(null).getSpecialEffects().stream().map(SpecialEffectsRegistry.REGISTRY::get).toList();
    }

    @Override
    public List<SpecialEffect> getRecipesForOutput(ITypedIngredient<?> iTypedIngredient) {
        return List.of();
    }

    @Override
    public List<SpecialEffect> getAllRecipes() {
        return SpecialEffectsRegistry.REGISTRY.stream().toList();
    }
}
