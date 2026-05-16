package net.yiran.jsb;

import mods.flammpfeil.slashblade.event.drop.EntityDropEntry;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;

public class Util {
    @SuppressWarnings("ConstantConditions")
    public static RegistryAccess getRegistryAccess() {
        return Minecraft.getInstance().getConnection().registryAccess();
    }

    public static Registry<EntityDropEntry> getEntityDropEntryRegistry() {
        return getRegistryAccess().registryOrThrow(EntityDropEntry.REGISTRY_KEY);
    }

    public static Registry<SlashBladeDefinition> getSlashBladeDefinitionRegistry() {
        return getRegistryAccess().registryOrThrow(SlashBladeDefinition.REGISTRY_KEY);
    }
}
