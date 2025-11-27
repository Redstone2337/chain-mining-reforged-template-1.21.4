// HomeData.java
package net.deepseek.v1.chainmining.core.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record HomeData(Vec3d position, RegistryKey<World> dimension, long timestamp) {

    // 创建 HomeData 的 Codec
    public static final Codec<HomeData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    // Vec3d 的序列化 - 使用三个 double 字段
                    Codec.DOUBLE.fieldOf("x").forGetter(data -> data.position.x),
                    Codec.DOUBLE.fieldOf("y").forGetter(data -> data.position.y),
                    Codec.DOUBLE.fieldOf("z").forGetter(data -> data.position.z),
                    // 维度的序列化
                    Identifier.CODEC.fieldOf("dimension").forGetter(data -> data.dimension.getValue()),
                    // 时间戳
                    Codec.LONG.fieldOf("timestamp").forGetter(HomeData::timestamp)
            ).apply(instance, (x, y, z, dimensionId, timestamp) ->
                    new HomeData(
                            new Vec3d(x, y, z),
                            RegistryKey.of(RegistryKeys.WORLD, dimensionId),
                            timestamp
                    )
            )
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeData homeData = (HomeData) o;
        return timestamp == homeData.timestamp &&
                position.equals(homeData.position) &&
                dimension.equals(homeData.dimension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, dimension, timestamp);
    }

    @Override
    public @NotNull String toString() {
        return String.format("HomeData{position=%s, dimension=%s, timestamp=%d}",
                position, dimension.getValue(), timestamp);
    }
}