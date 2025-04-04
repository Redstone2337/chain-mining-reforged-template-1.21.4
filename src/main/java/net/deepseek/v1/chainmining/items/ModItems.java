package net.deepseek.v1.chainmining.items;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.core.armor.ModArmorMaterials;
import net.deepseek.v1.chainmining.items.groups.ModItemGroups;
import net.minecraft.item.*;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class ModItems {
    public static final Item BEDROCK_SWORD = register(
            "bedrock_sword",
            (settings) -> new SwordItem(ModToolMaterial.BEDROCK,20,2.5F,
                    settings.maxDamage(700000).rarity(Rarity.EPIC)
            ));

    public static final Item BEDROCK_PICKAXE = register(
            "bedrock_pickaxe",
            (settings) -> new PickaxeItem(ModToolMaterial.BEDROCK,15,1.5F,
                    settings.maxDamage(6999000).rarity(Rarity.EPIC)
            ));

    public static final Item BEDROCK_AXE = register(
            "bedrock_axe",
            (settings) -> new AxeItem(ModToolMaterial.BEDROCK,25,4.5F,
                    settings.maxDamage(700200).rarity(Rarity.EPIC)
            ));

    public static final Item BEDROCK_SHOVEL = register(
            "bedrock_shovel",
            (settings) -> new ShovelItem(ModToolMaterial.BEDROCK,10,3.0F,
                    settings.maxDamage(675000).rarity(Rarity.EPIC)
            ));

    public static final Item BEDROCK_HOE = register(
            "bedrock_hoe",
            (settings) -> new HoeItem(ModToolMaterial.BEDROCK,10,3.5F,
                    settings.maxDamage(670000).rarity(Rarity.EPIC)
            ));

    public static final Item BEDROCKIUM_SWORD = register(
            "bedrockium_sword",
            (settings) -> new SwordItem(ModToolMaterial.BEDROCKIUM,20,2.5F,
                    settings.maxDamage(710000).rarity(Rarity.EPIC)
            ));

    public static final Item BEDROCKIUM_PICKAXE = register(
            "bedrockium_pickaxe",
            (settings) -> new PickaxeItem(ModToolMaterial.BEDROCKIUM,15,1.5F,
                    settings.maxDamage(7001000).rarity(Rarity.EPIC)
            ));

    public static final Item BEDROCKIUM_AXE = register(
            "bedrockium_axe",
            (settings) -> new AxeItem(ModToolMaterial.BEDROCKIUM,25,4.5F,
                    settings.maxDamage(710200).rarity(Rarity.EPIC)
            ));

    public static final Item BEDROCKIUM_SHOVEL = register(
            "bedrockium_shovel",
            (settings) -> new ShovelItem(ModToolMaterial.BEDROCKIUM,10,3.0F,
                    settings.maxDamage(685000).rarity(Rarity.EPIC)
            ));

    public static final Item BEDROCKIUM_HOE = register(
            "bedrockium_hoe",
            (settings) -> new HoeItem(ModToolMaterial.BEDROCKIUM,10,3.5F,
                    settings.maxDamage(680000).rarity(Rarity.EPIC)
            ));

    public static final Item BEDROCK_STONE = register("bedrock_stone",Item::new,
            new Item.Settings().maxCount(64).rarity(Rarity.EPIC));

    public static final Item BEDROCKIUM_STONE = register("bedrockium_blend",Item::new,
            new Item.Settings().maxCount(64).rarity(Rarity.EPIC));

    public static final Item DIAMOND_STICK = register("diamond_stick",Item::new,
            new Item.Settings().maxCount(64).rarity(Rarity.EPIC));

    public static final Item BEDROCK_HELMET = register("bedrock_helmet",
            (settings) -> new ArmorItem(ModArmorMaterials.BEDROCK, EquipmentType.HELMET,
                    settings.maxCount(1).rarity(Rarity.EPIC).fireproof()));

    public static final Item BEDROCK_CHESTPLATE = register("bedrock_chestplate",
            (settings) -> new ArmorItem(ModArmorMaterials.BEDROCK, EquipmentType.CHESTPLATE,
                    settings.maxCount(1).rarity(Rarity.EPIC).fireproof()));

    public static final Item BEDROCK_LEGGINGS = register("bedrock_leggings",
            (settings) -> new ArmorItem(ModArmorMaterials.BEDROCK, EquipmentType.LEGGINGS,
                    settings.maxCount(1).rarity(Rarity.EPIC).fireproof()));

    public static final Item BEDROCK_BOOTS = register("bedrock_boots",
            (settings) -> new ArmorItem(ModArmorMaterials.BEDROCK, EquipmentType.BOOTS,
                    settings.maxCount(1).rarity(Rarity.EPIC).fireproof()));

    public static final Item BEDROCKIUM_HELMET = register("bedrockium_helmet",
            (settings) -> new ArmorItem(ModArmorMaterials.BEDROCKIUM, EquipmentType.HELMET,
                    settings.maxCount(1).rarity(Rarity.EPIC).fireproof()));

    public static final Item BEDROCKIUM_CHESTPLATE = register("bedrockium_chestplate",
            (settings) -> new ArmorItem(ModArmorMaterials.BEDROCKIUM, EquipmentType.CHESTPLATE,
                    settings.maxCount(1).rarity(Rarity.EPIC).fireproof()));

    public static final Item BEDROCKIUM_LEGGINGS = register("bedrockium_leggings",
            (settings) -> new ArmorItem(ModArmorMaterials.BEDROCKIUM, EquipmentType.LEGGINGS,
                    settings.maxCount(1).rarity(Rarity.EPIC).fireproof()));

    public static final Item BEDROCKIUM_BOOTS = register("bedrockium_boots",
            (settings) -> new ArmorItem(ModArmorMaterials.BEDROCKIUM, EquipmentType.BOOTS,
                    settings.maxCount(1).rarity(Rarity.EPIC).fireproof()));



    private static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ChainMiningReforged.MOD_ID, id));
        return Items.register(registryKey, factory, settings);
    }

    private static Item register(String id, Function<Item.Settings, Item> factory) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ChainMiningReforged.MOD_ID, id));
        return Items.register(registryKey, factory, new Item.Settings());
    }

    public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = (Item) factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }
        return (Item) Registry.register(Registries.ITEM, key, item);
    }

    public static void register() {
        ChainMiningReforged.LOGGER.info("Registering items for " + ChainMiningReforged.MOD_ID);
    }
}
