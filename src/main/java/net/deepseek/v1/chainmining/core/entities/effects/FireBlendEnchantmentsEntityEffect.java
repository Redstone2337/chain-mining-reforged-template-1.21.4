package net.deepseek.v1.chainmining.core.entities.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public record FireBlendEnchantmentsEntityEffect(boolean FireBlend) implements EnchantmentEntityEffect {

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof LivingEntity target && FireBlend) {
            applyFireEffect(target, world);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }

    public static final MapCodec<FireBlendEnchantmentsEntityEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("FireBlend", true).forGetter(FireBlendEnchantmentsEntityEffect::FireBlend)
            ).apply(instance, FireBlendEnchantmentsEntityEffect::new)
    );

    public static void applyFireEffect(LivingEntity target, ServerWorld world) {
        // World world = target.getWorld();
        BlockPos basePos = target.getBlockPos();

        if (!world.isClient) {
            // 生成火焰环
            for (Direction dir : Direction.Type.HORIZONTAL) {
                BlockPos firePos = basePos.offset(dir);
                if (world.getBlockState(firePos).isAir()) {
                    world.setBlockState(firePos, Blocks.FIRE.getDefaultState());
                }
            }

            // 生成岩浆方块
            BlockPos magmaPos = basePos.down();
            if (world.getBlockState(magmaPos).isAir()) {
                world.setBlockState(magmaPos, Blocks.MAGMA_BLOCK.getDefaultState());
            }

            // 限制移动
            target.setVelocity(Vec3d.ZERO);
            target.velocityModified = true;
            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    100,
                    255,
                    false,
                    false
            ));
        }
    }
}
