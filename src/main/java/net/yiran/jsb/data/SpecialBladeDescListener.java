package net.yiran.jsb.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;

public class SpecialBladeDescListener extends SimpleJsonResourceReloadListener {
    public static List<SpecialBladeDescData> DESC_DATA = new ObjectArrayList<>();

    public SpecialBladeDescListener() {
        super(GsonHelper.GSON, "blade_desc");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        DESC_DATA.clear();
        for (JsonElement value : map.values()) {
            SpecialBladeDescData.CODEC.parse(JsonOps.INSTANCE, value).result().ifPresent(DESC_DATA::add);
        }
    }

}
