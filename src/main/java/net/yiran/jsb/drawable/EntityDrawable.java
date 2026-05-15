package net.yiran.jsb.drawable;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.yiran.jsb.Config;
import net.yiran.jsb.data.ShowEntityListener;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class EntityDrawable implements IDrawable {
    public static Map<EntityType<?>, LivingEntity> ENTITY_CACHE = new HashMap<>();
    public EntityType<?> entityType;
    public LivingEntity renderEntity;
    public float renderTicks = 0;

    public EntityDrawable(EntityType<?> entityType) {
        this.entityType = entityType;
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

        guiGraphics.pose().translate(35, 0, 0);
        var data = ShowEntityListener.getShowData(entityType);
        int scale;
        if (data != null) {
            guiGraphics.pose().translate(0, data.yOffset(), 0);
            scale = data.scale();
        } else {
            scale = (int) Math.min(120 / box.getXsize(), 50 / box.getYsize());
        }


        float speed = Config.ENTITY_ROTATION_SPEED.get().floatValue();
        if (speed != 0) {
            renderTicks += Minecraft.getInstance().getPartialTick();
            guiGraphics.pose().translate(0, 0, 40);
            guiGraphics.pose().mulPose(new Quaternionf().rotateY(renderTicks * speed));
            guiGraphics.pose().translate(0, 0, -40);
        }


        InventoryScreen.renderEntityInInventoryFollowsAngle(
                guiGraphics, 0, 80, scale,
                -1,
                0.56f,
                renderEntity
        );
    }
}
