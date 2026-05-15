package net.yiran.jsb.drawable;

import mezz.jei.api.gui.drawable.IDrawable;
import mods.flammpfeil.slashblade.event.drop.EntityDropEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class EntityDrawable implements IDrawable {
    public static Map<EntityType<?>, LivingEntity> ENTITY_CACHE = new HashMap<>();
    public LivingEntity renderEntity;

    public EntityDrawable(EntityType<?> entityType) {
        if (!ENTITY_CACHE.containsKey(entityType)) {
            ENTITY_CACHE.put(entityType, (LivingEntity) entityType.create(Minecraft.getInstance().level));
        }
        renderEntity = ENTITY_CACHE.get(entityType);
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int i, int i1) {
        var box = renderEntity.getBoundingBox();
        guiGraphics.pose().mulPose(new Quaternionf().rotateY(0.161f));
        int scale = (int) Math.min(120 / box.getXsize(), 50 / box.getYsize());

        InventoryScreen.renderEntityInInventoryFollowsAngle(
                guiGraphics, 35, (int) (35 + box.getYsize() * scale), scale,
                -1,
                0.56f,
                renderEntity
        );
    }
}
