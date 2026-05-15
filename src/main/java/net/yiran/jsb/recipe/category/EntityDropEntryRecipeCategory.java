package net.yiran.jsb.recipe.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.gui.elements.DrawableText;
import mods.flammpfeil.slashblade.event.drop.EntityDropEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.yiran.jsb.JEISlashBlade;
import net.yiran.jsb.drawable.EntityDrawable;
import net.yiran.jsb.recipe.BladeRegisterManager;

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
        if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entityDropEntry.entityType())) {
            entityItem = Items.BARRIER;
            entitySlot.addTooltipCallback((iRecipeSlotView, components) -> {
                components.clear();
                components.add(Component.translatable("jsb.entity.not_registered", entityDropEntry.entityType()));
            });
        } else {
            var entityType = BuiltInRegistries.ENTITY_TYPE.get(entityDropEntry.entityType());
            entityItem = SpawnEggItem.byId(entityType);
            if (entityItem == null) {
                entityItem = Items.STRUCTURE_VOID;
            }
            entitySlot.addTooltipCallback((iRecipeSlotView, components) -> {
                components.clear();
                components.add(Component.translatable(entityType.getDescriptionId()));
            });
        }
        entitySlot.addItemLike(entityItem);
        var blade = BladeRegisterManager.getBlade(entityDropEntry.bladeName());
        IRecipeSlotBuilder bladeSlot = builder.addOutputSlot(64 - 8, 6)
                .setStandardSlotBackground();
        if (blade == null) {
            bladeSlot.addTooltipCallback((iRecipeSlotView, components) -> {
                        components.clear();
                        components.add(Component.translatable("jsb.blade.not_registered", entityDropEntry.bladeName()));
                    })
                    .addItemLike(Items.BARRIER);
        } else {
            bladeSlot.addItemStack(blade);
        }
        entityDropEntry.dropRate();
        entityDropEntry.requestSlashBladeKill();
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, EntityDropEntry recipe, IFocusGroup focuses) {

        if (BuiltInRegistries.ENTITY_TYPE.containsKey(recipe.entityType())) {
            builder.addDrawable(new EntityDrawable(BuiltInRegistries.ENTITY_TYPE.get(recipe.entityType())), 0, 0);
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
