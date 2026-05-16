package net.yiran.jsb.recipe;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.yiran.jsb.Util;

import java.util.Collection;
import java.util.Map;

public class BladeRegisterManager {
    public static Map<ResourceLocation, ItemStack> CACHE = new Object2ObjectOpenHashMap<>();

    public static ItemStack getBlade(ResourceLocation resourceLocation) {
        return CACHE.get(resourceLocation);
    }

    public static Collection<ItemStack> getAllBlades() {
        return CACHE.values();
    }

    public static void build() {
        CACHE.clear();
        Util.getSlashBladeDefinitionRegistry()
                .entrySet()
                .forEach(entry -> CACHE.put(entry.getKey().location(), entry.getValue().getBlade()));
    }
}
