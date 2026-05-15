package net.yiran.jsb.recipe;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
        SlashBlade.getSlashBladeDefinitionRegistry(Minecraft.getInstance().level)
                .entrySet().stream()
                .forEach(entry -> CACHE.put(entry.getKey().location(), entry.getValue().getBlade(Minecraft.getInstance().getConnection().registryAccess())));
    }
}
