package net.deepseek.v1.chainmining.core.entities.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record TestEnchantmentsEffect(boolean isEnable) implements EnchantmentEntityEffect {

    public static final MapCodec<TestEnchantmentsEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("isEnable",true).forGetter(TestEnchantmentsEffect::isEnable)
            ).apply(instance,TestEnchantmentsEffect::new)
    );



    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof LivingEntity && !(user instanceof PlayerEntity) && isEnable) {
//            if (level == 1) {
//                user.setFireTicks(200);
//                user.setOnFireFor(200.0f);
//            }
//            int lvl = (int) max.getValue(level);
            switch (level) {
                case 1:
                    user.setFireTicks(200);
                    user.setOnFireFor(200.0f);
                    break;
                case 2:
                    user.setFireTicks(400);
                    user.setOnFireFor(400.f);
                    break;
                case 3:
                    user.setFireTicks(600);
                    user.setOnFireFor(600.0f);
                    break;
                default:
                    user.setFireTicks(150);
                    user.setOnFireFor(150.0f);
                    break;

            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
