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
import net.minecraft.util.math.Vec3d;

public record IceBlendEnchantmentsEntityEffect(boolean IceBlend) implements EnchantmentEntityEffect {

    public static final MapCodec<IceBlendEnchantmentsEntityEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("IceBlend", true).forGetter(IceBlendEnchantmentsEntityEffect::IceBlend)
            ).apply(instance, IceBlendEnchantmentsEntityEffect::new)
    );


    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof LivingEntity target && IceBlend) {
            //World world = target.getWorld();
//            BlockPos basePos = target.getBlockPos();
//
//            if (!world.isClient) {
//                // 生成霜冰结构
//                for (int y = 0; y < 3; y++) {
//                    BlockPos currentPos = basePos.up(y);
//                    if (world.getBlockState(currentPos).isAir()) {
//                        world.setBlockState(currentPos, Blocks.BLUE_ICE.getDefaultState());
//                    }
//                }
//
//                // 完全冻结实体
//                target.setVelocity(Vec3d.ZERO);
//                target.velocityModified = true;
//                target.addStatusEffect(new StatusEffectInstance(
//                        StatusEffects.SLOWNESS,
//                        100,
//                        255,
//                        false,
//                        false
//                ));
//                target.addStatusEffect(new StatusEffectInstance(
//                        StatusEffects.MINING_FATIGUE,
//                        100,
//                        255,
//                        false,
//                        false
//                ));
//            }
            applyIceEffect(target,world);
        }
    }

        @Override
        public MapCodec<? extends EnchantmentEntityEffect> getCodec () {
            return CODEC;
    }

    public static void applyIceEffect(LivingEntity target, ServerWorld world) {
        // 获取目标实体的世界和位置
//        World world = target.getWorld();
        BlockPos basePos = target.getBlockPos();

        if (!world.isClient) {
            // 生成霜冰结构
            for (int y = 0; y < 3; y++) {
                BlockPos currentPos = basePos.up(y);
                if (world.getBlockState(currentPos).isAir()) {
                    world.setBlockState(currentPos, Blocks.BLUE_ICE.getDefaultState());
                }
            }

            // 完全冻结实体
            target.setVelocity(Vec3d.ZERO);
            target.velocityModified = true;
            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    100,
                    255,
                    false,
                    false
            ));
            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.MINING_FATIGUE,
                    100,
                    255,
                    false,
                    false
            ));
        }
    }
}
