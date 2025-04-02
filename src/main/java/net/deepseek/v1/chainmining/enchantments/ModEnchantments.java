package net.deepseek.v1.chainmining.enchantments;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.core.entities.effects.*;
import net.deepseek.v1.chainmining.tag.ModItemTags;
import net.minecraft.block.Block;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class ModEnchantments {

    public static final RegistryKey<Enchantment> FIRE_FOR_ONESELF = of("fire_for_oneself");
    public static final RegistryKey<Enchantment> ONE_PUSH = of("one_push");
    public static final RegistryKey<Enchantment> ICE_BLEND = of("ice_blend");
    public static final RegistryKey<Enchantment> FIRE_BLEND = of("fire_blend");
    public static final RegistryKey<Enchantment> ICE_FIRE_BLEND = of("ice_fire_blend");


    public static void bootstrap(Registerable<Enchantment> registry) {
        RegistryEntryLookup<DamageType> registryEntryLookup = registry.getRegistryLookup(RegistryKeys.DAMAGE_TYPE);
        RegistryEntryLookup<Enchantment> registryEntryLookup2 = registry.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        RegistryEntryLookup<Item> registryEntryLookup3 = registry.getRegistryLookup(RegistryKeys.ITEM);
        RegistryEntryLookup<Block> registryEntryLookup4 = registry.getRegistryLookup(RegistryKeys.BLOCK);
        RegistryEntryLookup<EntityType<?>> registryEntryLookup5 = registry.getRegistryLookup(RegistryKeys.ENTITY_TYPE);
        register(registry,
                FIRE_FOR_ONESELF,
                Enchantment.builder(
                                Enchantment.definition(
                                        registryEntryLookup3.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                        registryEntryLookup3.getOrThrow(ModItemTags.SWORD),
                                        10,
                                        3,
                                        Enchantment.leveledCost(1,10),
                                        Enchantment.leveledCost(1,15),
                                        5,
                                        AttributeModifierSlot.MAINHAND
                                )
                        )
                        .addEffect(
                                EnchantmentEffectComponentTypes.POST_ATTACK,
                                EnchantmentEffectTarget.ATTACKER,
                                EnchantmentEffectTarget.VICTIM,
                                new TestEnchantmentsEffect(true)
                        )

        );
        register(registry,
                ONE_PUSH,
                Enchantment.builder(
                        Enchantment.definition(
                                registryEntryLookup3.getOrThrow(ModItemTags.SWORD),
                                registryEntryLookup3.getOrThrow(ModItemTags.FUNCTION_LIST),
                                10,
                                1,
                                Enchantment.leveledCost(1,5),
                                Enchantment.leveledCost(2,4),
                                5,
                                AttributeModifierSlot.HAND
                        )
                )
                        .addEffect(
                                EnchantmentEffectComponentTypes.POST_ATTACK,
                                EnchantmentEffectTarget.ATTACKER,
                                EnchantmentEffectTarget.VICTIM,
                                new OnePush(true)
                        )
        );
        register(registry,
                ICE_BLEND,
                Enchantment.builder(
                        Enchantment.definition(
                                registryEntryLookup3.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                registryEntryLookup3.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                10,
                                1,
                                Enchantment.leveledCost(1,5),
                                Enchantment.leveledCost(2,4),
                                5,
                                AttributeModifierSlot.MAINHAND
                        )
                )
                        .addEffect(
                                EnchantmentEffectComponentTypes.POST_ATTACK,
                                EnchantmentEffectTarget.ATTACKER,
                                EnchantmentEffectTarget.VICTIM,
                                new IceBlendEnchantmentsEntityEffect(true)
                        )
        );
        register(registry,
                FIRE_BLEND,
                Enchantment.builder(
                        Enchantment.definition(
                                registryEntryLookup3.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                registryEntryLookup3.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                10,
                                1,
                                Enchantment.leveledCost(1,5),
                                Enchantment.leveledCost(2,4),
                                5,
                                AttributeModifierSlot.MAINHAND
                        )
                )
                        .addEffect(
                                EnchantmentEffectComponentTypes.POST_ATTACK,
                                EnchantmentEffectTarget.ATTACKER,
                                EnchantmentEffectTarget.VICTIM,
                                new FireBlendEnchantmentsEntityEffect(true)
                        )
        );
        register(registry,
                ICE_FIRE_BLEND,
                Enchantment.builder(
                        Enchantment.definition(
                                registryEntryLookup3.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                registryEntryLookup3.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                10,
                                1,
                                Enchantment.leveledCost(1,5),
                                Enchantment.leveledCost(2,4),
                                5,
                                AttributeModifierSlot.MAINHAND
                        )
                )
                        .addEffect(
                                EnchantmentEffectComponentTypes.POST_ATTACK,
                                EnchantmentEffectTarget.ATTACKER,
                                EnchantmentEffectTarget.VICTIM,
                                new IceFireBlendEnchantmentsEntityEffect(true, true)
                        )
        );
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }

    private static RegistryKey<Enchantment> of(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(ChainMiningReforged.MOD_ID, id));
    }

    public static void register() {
        ChainMiningReforged.LOGGER.info("注册附魔成功！");
    }
}
