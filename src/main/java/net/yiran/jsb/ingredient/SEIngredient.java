package net.yiran.jsb.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import mods.flammpfeil.slashblade.registry.SpecialEffectsRegistry;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

@SuppressWarnings({"all", "removal"})
public class SEIngredient implements IIngredientType<SpecialEffect>, IIngredientHelper<SpecialEffect>, IIngredientRenderer<SpecialEffect> {
    public static SEIngredient INSTANCE = new SEIngredient();
    public static Lazy<ItemStack> renderItem = Lazy.of(() -> SlashBladeItems.PROUDSOUL_CRYSTAL.get().getDefaultInstance());

    @Override
    public IIngredientType<SpecialEffect> getIngredientType() {
        return null;
    }

    @Override
    public String getDisplayName(SpecialEffect specialEffect) {
        return specialEffect.getDescription().getString();
    }

    @Override
    public boolean hasSubtypes(SpecialEffect ingredient) {
        return true;
    }

    @Override
    public String getUniqueId(SpecialEffect specialEffect, UidContext uidContext) {
        return SpecialEffectsRegistry.REGISTRY.get().getKey(specialEffect).toString();
    }

    @Override
    public ResourceLocation getResourceLocation(SpecialEffect specialEffect) {
        return SpecialEffectsRegistry.REGISTRY.get().getKey(specialEffect);
    }

    @Override
    public SpecialEffect copyIngredient(SpecialEffect specialEffect) {
        return specialEffect;
    }

    @Override
    public String getErrorInfo(SpecialEffect specialEffect) {
        return "SA Ingredient Error";
    }

    @Override
    public void render(GuiGraphics guiGraphics, SpecialEffect specialEffect) {
        guiGraphics.renderItem(renderItem.get(), 0, 0);
        Decoration.renderSEDecorator(guiGraphics, Minecraft.getInstance().font, specialEffect);
    }

    @Override
    public List<Component> getTooltip(SpecialEffect specialEffect, TooltipFlag tooltipFlag) {
        return List.of(specialEffect.getDescription());
    }

    @Override
    public Class<? extends SpecialEffect> getIngredientClass() {
        return SpecialEffect.class;
    }
}
