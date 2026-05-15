package net.yiran.jsb.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

public record ShowEntityData(EntityType<?> entityType, int scale, float yOffset) {
    public static final Codec<ShowEntityData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entityType").forGetter(ShowEntityData::entityType),
            Codec.INT.fieldOf("scale").forGetter(ShowEntityData::scale),
            Codec.FLOAT.optionalFieldOf("yOffset", 0f).forGetter(ShowEntityData::yOffset)
    ).apply(instance, ShowEntityData::new));
}
