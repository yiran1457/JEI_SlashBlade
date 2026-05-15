package net.yiran.jsb.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record SpecialBladeDescData(ResourceLocation bladeName, String description) {
    public static Codec<SpecialBladeDescData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("bladeName").forGetter(SpecialBladeDescData::bladeName),
            Codec.STRING.fieldOf("description").forGetter(SpecialBladeDescData::description)
    ).apply(instance, SpecialBladeDescData::new));
}
