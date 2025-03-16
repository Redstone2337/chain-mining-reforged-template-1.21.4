package net.deepseek.v1.chainmining.core.entities;

import com.mojang.serialization.MapCodec;
import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.core.entities.effects.TestEnchantmentsEffect;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantmentEffects {
    private static void register(String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        //TODO: Implement enchantment effects
        Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(ChainMiningReforged.MOD_ID, name),codec);
    }

    public static void register() {
        register("fire_foe_oneself", TestEnchantmentsEffect.CODEC);

        ChainMiningReforged.LOGGER.info("附魔效果注册成功！");
    }

}
