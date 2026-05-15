package net.yiran.jsb;

import mods.flammpfeil.slashblade.event.drop.EntityDropEntry;
import net.minecraft.client.Minecraft;
import net.yiran.jsb.recipe.category.EntityDropEntryRecipeCategory;
import net.yiran.jsb.recipe.category.SARecipeCategory;
import net.yiran.jsb.recipe.category.SERecipeCategory;
import net.yiran.jsb.ingredient.SAIngredient;
import net.yiran.jsb.ingredient.SEIngredient;
import net.yiran.jsb.recipe.manager.SARecipeManager;
import net.yiran.jsb.recipe.manager.SERecipeManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.SpecialEffectsRegistry;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class SlashBladePlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.parse("jei_slashblade:sase");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(SARecipeCategory.SA_TYPE, SlashArtsRegistry.REGISTRY.get().getValues().stream().toList());
        registration.addRecipes(SERecipeCategory.SE_TYPE, SpecialEffectsRegistry.REGISTRY.get().getValues().stream().toList());
        registration.addRecipes(EntityDropEntryRecipeCategory.DROP_TYPE, Minecraft.getInstance().getConnection().registryAccess().registryOrThrow(EntityDropEntry.REGISTRY_KEY).stream().toList());

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(SARecipeCategory.INSTANCE, SERecipeCategory.INSTANCE, EntityDropEntryRecipeCategory.INSTANCE);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(SAIngredient.INSTANCE, SlashArtsRegistry.REGISTRY.get().getValues(), SAIngredient.INSTANCE, SAIngredient.INSTANCE);
        registration.register(SEIngredient.INSTANCE, SpecialEffectsRegistry.REGISTRY.get().getValues(), SEIngredient.INSTANCE, SEIngredient.INSTANCE);
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        registration.addTypedRecipeManagerPlugin(SARecipeCategory.SA_TYPE, SARecipeManager.INSTANCE);
        registration.addTypedRecipeManagerPlugin(SERecipeCategory.SE_TYPE, SERecipeManager.INSTANCE);
    }

}
