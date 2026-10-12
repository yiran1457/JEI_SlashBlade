package net.yiran.jsb.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import mods.flammpfeil.slashblade.event.drop.EntityDropEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;
import net.yiran.jsb.recipe.BladeRegisterManager;

import java.util.List;

public class SlashBladeDropEmiRecipe implements EmiRecipe {
    private final EntityDropEntry dropEntry;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public SlashBladeDropEmiRecipe(EntityDropEntry dropEntry) {
        this.dropEntry = dropEntry;
        this.id = ResourceLocation.parse("jei_slashblade:/drop/" + dropEntry.bladeName().getNamespace() + "/" + dropEntry.bladeName().getPath() + "/" + dropEntry.entityType().getNamespace() + "/" + dropEntry.entityType().getPath());

        Item entityItem;
        if (!ForgeRegistries.ENTITY_TYPES.containsKey(dropEntry.entityType())) {
            entityItem = Items.BARRIER;
        } else {
            var entityType = ForgeRegistries.ENTITY_TYPES.getValue(dropEntry.entityType());
            entityItem = ForgeSpawnEggItem.fromEntityType(entityType);
            if (entityItem == null) {
                entityItem = Items.STRUCTURE_VOID;
            }
        }
        this.inputs = List.of(EmiStack.of(entityItem));

        ItemStack blade = BladeRegisterManager.getBlade(dropEntry.bladeName());
        if (blade == null) {
            blade = Items.BARRIER.getDefaultInstance();
        }
        this.outputs = List.of(EmiStack.of(blade));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return SlashBladeEmiPlugin.DROP_CATEGORY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return 130;
    }

    @Override
    public int getDisplayHeight() {
        return 48;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        // 实体刷怪蛋/物品（输入）
        widgets.addSlot(inputs.get(0), 12, 14);

        // 箭头
        widgets.addFillingArrow(46, 14, 2000);

        // 掉落概率
        String rateText = String.format("%.2f%%", dropEntry.dropRate() * 100);
        widgets.addText(Component.literal(rateText), 58, 2, 0xFFFFAA00, true);

        // 是否需要拔刀击杀
        if (dropEntry.requestSlashBladeKill()) {
            widgets.addText(Component.literal("需要拔刀击杀"), 40, 36, 0xFFFF5555, false);
        }

        // 掉落拔刀剑（输出）
        widgets.addSlot(outputs.get(0), 92, 14).recipeContext(this);
    }
}
