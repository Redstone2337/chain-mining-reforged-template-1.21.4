package net.deepseek.v1.chainmining.core.armor;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

import static net.minecraft.item.equipment.EquipmentAssetKeys.REGISTRY_KEY;

public interface ModEquipmentAssetKeys {

    RegistryKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY = RegistryKey.ofRegistry(Identifier.of(ChainMiningReforged.MOD_ID,"equipment"));
    RegistryKey<EquipmentAsset> BEDROCK_ASSET = register("bedrock");
    RegistryKey<EquipmentAsset> BEDROCKIUM_ASSET = register("bedrockium");
    RegistryKey<EquipmentAsset> BEDROCKIUM_ARROW = register("bedrockium_arrow");

    static void accept(BiConsumer<RegistryKey<EquipmentAsset>, EquipmentModel> equipmentBiConsumer) {
        equipmentBiConsumer.accept(BEDROCK_ASSET, createHumanoidOnlyModel("bedrock"));
        equipmentBiConsumer.accept(BEDROCKIUM_ASSET, createHumanoidOnlyModel("bedrockium"));
    }

    static EquipmentModel createHumanoidOnlyModel(String id) {
        return EquipmentModel.builder().addHumanoidLayers(Identifier.of(ChainMiningReforged.MOD_ID, id)).build();
    }

    static RegistryKey<EquipmentAsset> register(String name) {
        return RegistryKey.of(REGISTRY_KEY, Identifier.of(ChainMiningReforged.MOD_ID,name));
    }
}

