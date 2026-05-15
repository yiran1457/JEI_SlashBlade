package net.yiran.jsb.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

public class ShowEntityListener extends SimpleJsonResourceReloadListener {
    public static Map<EntityType<?>, ShowEntityData> SHOW_DATA = new Object2ObjectOpenHashMap<>();

    public ShowEntityListener() {
        super(GsonHelper.GSON, "show_entity");
    }

    public static ShowEntityData getShowData(EntityType<?> entityType) {
        return SHOW_DATA.get(entityType);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        SHOW_DATA.clear();
        for (JsonElement value : map.values()) {
            ShowEntityData.CODEC.parse(JsonOps.INSTANCE, value).result().ifPresent(specialBladeDescData -> {
                SHOW_DATA.put(specialBladeDescData.entityType(), specialBladeDescData);
            });
        }
    }
}
