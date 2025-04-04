package net.deepseek.v1.chainmining.core.entities.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record IceFireBlendEnchantmentsEntityEffect(boolean IceBlend, boolean FireBlend) implements EnchantmentEntityEffect {
    public static final MapCodec<IceFireBlendEnchantmentsEntityEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("IceBlend", true).forGetter(IceFireBlendEnchantmentsEntityEffect::IceBlend),
                    Codec.BOOL.optionalFieldOf("FireBlend", true).forGetter(IceFireBlendEnchantmentsEntityEffect::FireBlend)
            ).apply(instance, IceFireBlendEnchantmentsEntityEffect::new)
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof LivingEntity target) {
            if (IceBlend) {
                applyIceEffect(target, world);
            } else if (FireBlend) {
                applyFireEffect(target, world);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }

    private static void applyFireEffect(LivingEntity target, ServerWorld world) {
        FireBlendEnchantmentsEntityEffect.applyFireEffect(target,world);
    }

    private static void applyIceEffect(LivingEntity target, ServerWorld world) {
        IceBlendEnchantmentsEntityEffect.applyIceEffect(target,world);
    }
}
