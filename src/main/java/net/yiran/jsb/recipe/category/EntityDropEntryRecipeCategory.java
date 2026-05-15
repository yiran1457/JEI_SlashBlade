package net.yiran.jsb.recipe.category;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.gui.elements.DrawableText;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.event.drop.EntityDropEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;
import net.yiran.jsb.JEISlashBlade;
import net.yiran.jsb.drawable.EntityDrawable;
import org.joml.Quaternionf;

@SuppressWarnings({"removal", "all"})
public class EntityDropEntryRecipeCategory implements IRecipeCategory<EntityDropEntry> {
    public static EntityDropEntryRecipeCategory INSTANCE = new EntityDropEntryRecipeCategory();
    public static RecipeType<EntityDropEntry> DROP_TYPE = RecipeType.create(JEISlashBlade.MODID, "drop", EntityDropEntry.class);

    @Override
    public RecipeType<EntityDropEntry> getRecipeType() {
        return DROP_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("DROP");
    }

    @Override
    public int getWidth() {
        return 16 * 5;
    }

    @Override
    public int getHeight() {
        return 88;//30;
    }

    @Override
    public IDrawable getIcon() {
        return new DrawableText("DROP", 16, 16, -1);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EntityDropEntry entityDropEntry, IFocusGroup iFocusGroup) {
        Item entityItem;
        IRecipeSlotBuilder entitySlot = builder.addInputSlot(8, 6)
                .setStandardSlotBackground();
        if (!ForgeRegistries.ENTITY_TYPES.containsKey(entityDropEntry.entityType())) {
            entityItem = Items.BARRIER;
            entitySlot.addTooltipCallback((iRecipeSlotView, components) -> {
                components.clear();
                components.add(Component.translatable("jsb.entity.not_registered", entityDropEntry.entityType()));
            });
        } else {
            var entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityDropEntry.entityType());
            entityItem = ForgeSpawnEggItem.fromEntityType(entityType);
            if (entityItem == null) {
                entityItem = Items.STRUCTURE_VOID;
            }
            entitySlot.addTooltipCallback((iRecipeSlotView, components) -> {
                components.clear();
                components.add(Component.translatable(entityType.getDescriptionId()));
            });
        }
        entitySlot.addItemLike(entityItem);
        var bladeInfo = SlashBlade.getSlashBladeDefinitionRegistry(Minecraft.getInstance().level).get(entityDropEntry.bladeName());
        IRecipeSlotBuilder bladeSlot = builder.addOutputSlot(64 - 8, 6)
                .setStandardSlotBackground();
        if (bladeInfo == null) {
            bladeSlot.addTooltipCallback((iRecipeSlotView, components) -> {
                        components.clear();
                        components.add(Component.translatable("jsb.blade.not_registered", entityDropEntry.bladeName()));
                    })
                    .addItemLike(Items.BARRIER);
        } else {
            bladeSlot.addItemStack(bladeInfo.getBlade());
        }
        entityDropEntry.dropRate();
        entityDropEntry.requestSlashBladeKill();
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, EntityDropEntry recipe, IFocusGroup focuses) {

        if (ForgeRegistries.ENTITY_TYPES.containsKey(recipe.entityType())) {
            builder.addDrawable(new EntityDrawable(ForgeRegistries.ENTITY_TYPES.getValue(recipe.entityType())), 0, 0);
        }
        builder.addRecipeArrow()
                .setPosition(29, 6);
        builder.addText(Component.literal(String.format("%.2f", recipe.dropRate() * 100) + "%"), getWidth(), 10)
                .setTextAlignment(HorizontalAlignment.CENTER)
                .setPosition(0, -2);
        if (recipe.requestSlashBladeKill()) {
            builder.addText(Component.literal("需要拔刀击杀"), getWidth(), 10)
                    .setTextAlignment(HorizontalAlignment.CENTER)
                    .setPosition(0, 23);
        }

    }

}
