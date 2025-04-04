package net.deepseek.v1.chainmining.core.armor;

import net.deepseek.v1.chainmining.tag.ModItemTags;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.Map;

public interface ModArmorMaterials {
    ArmorMaterial BEDROCK = new ArmorMaterial(33, (Map) Util.make(new EnumMap(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 5);
        map.put(EquipmentType.LEGGINGS, 6);
        map.put(EquipmentType.CHESTPLATE, 7);
        map.put(EquipmentType.HELMET, 5);
        map.put(EquipmentType.BODY, 8);
    }), 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.0F, 1.0F, ModItemTags.REPAIRS_BEDROCK_ARMOR, ModEquipmentAssetKeys.BEDROCK_ASSET);

    ArmorMaterial BEDROCKIUM = new ArmorMaterial(35, (Map) Util.make(new EnumMap(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 5);
        map.put(EquipmentType.LEGGINGS, 6);
        map.put(EquipmentType.CHESTPLATE, 7);
        map.put(EquipmentType.HELMET, 5);
        map.put(EquipmentType.BODY, 8);
    }), 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.5F, 1.5F, ModItemTags.REPAIRS_BEDROCKIUM_ARMOR, ModEquipmentAssetKeys.BEDROCKIUM_ASSET);
}
