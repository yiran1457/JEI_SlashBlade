package net.yiran.jsb;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.event.drop.EntityDropEntry;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.SpecialEffectsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.yiran.jsb.data.SpecialBladeDescListener;
import net.yiran.jsb.ingredient.SAIngredient;
import net.yiran.jsb.ingredient.SEIngredient;
import net.yiran.jsb.recipe.BladeRegisterManager;
import net.yiran.jsb.recipe.category.EntityDropEntryRecipeCategory;
import net.yiran.jsb.recipe.category.SARecipeCategory;
import net.yiran.jsb.recipe.category.SERecipeCategory;
import net.yiran.jsb.recipe.category.SpecialBladeDescRecipeCategory;
import net.yiran.jsb.recipe.manager.SARecipeManager;
import net.yiran.jsb.recipe.manager.SERecipeManager;

import java.util.stream.Stream;

@SuppressWarnings("ConstantConditions")
@JeiPlugin
public class SlashBladePlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.parse("jei_slashblade:sase");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        BladeRegisterManager.build();
        registration.addRecipes(SARecipeCategory.SA_TYPE, SlashArtsRegistry.REGISTRY.get().getValues().stream().toList());
        registration.addRecipes(SERecipeCategory.SE_TYPE, SpecialEffectsRegistry.REGISTRY.get().getValues().stream().toList());
        Stream<EntityDropEntry> drops = Minecraft.getInstance().getConnection().registryAccess().registryOrThrow(EntityDropEntry.REGISTRY_KEY).stream();
        if (Config.FILTER_DROP.get()) {
            drops = drops.filter(entityDropEntry -> {
                if (!ForgeRegistries.ENTITY_TYPES.containsKey(entityDropEntry.entityType())) return false;
                return SlashBlade.getSlashBladeDefinitionRegistry(Minecraft.getInstance().level).containsKey(entityDropEntry.bladeName());
            });
        }
        registration.addRecipes(EntityDropEntryRecipeCategory.DROP_TYPE, drops.toList());
        registration.addRecipes(SpecialBladeDescRecipeCategory.DESC_TYPE, SpecialBladeDescListener.DESC_DATA);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(SARecipeCategory.INSTANCE, SERecipeCategory.INSTANCE, EntityDropEntryRecipeCategory.INSTANCE, SpecialBladeDescRecipeCategory.INSTANCE);
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
