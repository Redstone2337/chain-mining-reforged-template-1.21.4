package net.deepseek.v1.chainmining.core.entities.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record OnePush(boolean isOnePush) implements EnchantmentEntityEffect {
    public static final MapCodec<OnePush> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("isOnePush",true).forGetter(OnePush::isOnePush)
            ).apply(instance,OnePush::new)
    );


    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof PlayerEntity && isOnePush) {
            LivingEntity living = (LivingEntity) user;
            user.setVelocity(0, 1, 0);
            living.kill(world);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
