package net.yiran.jsb.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.List;

@SuppressWarnings({"all", "removal"})
public class SAIngredient implements IIngredientType<SlashArts>, IIngredientHelper<SlashArts>, IIngredientRenderer<SlashArts> {
    public static SAIngredient INSTANCE = new SAIngredient();
    public static Lazy<ItemStack> renderItem = Lazy.of(() -> SlashBladeItems.PROUDSOUL_SPHERE.get().getDefaultInstance());

    @Override
    public IIngredientType<SlashArts> getIngredientType() {
        return null;
    }

    @Override
    public String getDisplayName(SlashArts slashArts) {
        return slashArts.getDescription().getString();
    }

    @Override
    public boolean hasSubtypes(SlashArts ingredient) {
        return true;
    }

    @Override
    public String getUniqueId(SlashArts slashArts, UidContext uidContext) {
        return SlashArtsRegistry.REGISTRY.getKey(slashArts).toString();
    }

    @Override
    public ResourceLocation getResourceLocation(SlashArts slashArts) {
        return SlashArtsRegistry.REGISTRY.getKey(slashArts);
    }

    @Override
    public SlashArts copyIngredient(SlashArts slashArts) {
        return slashArts;
    }

    @Override
    public String getErrorInfo(SlashArts slashArts) {
        return "SA Ingredient Error";
    }

    @Override
    public void render(GuiGraphics guiGraphics, SlashArts slashArts) {
        guiGraphics.renderItem(renderItem.get(),0,0 );
        Decoration.renderSADecorator(guiGraphics, Minecraft.getInstance().font, slashArts);
    }

    @Override
    public List<Component> getTooltip(SlashArts slashArts, TooltipFlag tooltipFlag) {
        return List.of(slashArts.getDescription());
    }

    @Override
    public Class<? extends SlashArts> getIngredientClass() {
        return SlashArts.class;
    }
}
