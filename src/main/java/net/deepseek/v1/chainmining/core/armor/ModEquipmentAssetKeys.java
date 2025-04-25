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
    Identifier BEDROCK = Identifier.of(ChainMiningReforged.MOD_ID, "bedrock");
    Identifier BEDROCKIUM = Identifier.of(ChainMiningReforged.MOD_ID, "bedrockium");


    EquipmentModel BEDROCK_MODEL = buildHumanoid("bedrock");
    EquipmentModel BEDROCKIUM_MODEL = buildHumanoid("bedrockium");

    RegistryKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY = RegistryKey.ofRegistry(Identifier.of(ChainMiningReforged.MOD_ID,"equipment"));
    RegistryKey<EquipmentAsset> BEDROCK_ASSET = register("bedrock");
    RegistryKey<EquipmentAsset> BEDROCKIUM_ASSET = register("bedrockium");
    RegistryKey<EquipmentAsset> BEDROCKIUM_ARROW = register("bedrockium_arrow");

    static void accept(BiConsumer<Identifier, EquipmentModel> equipmentModelBiConsumer) {
        equipmentModelBiConsumer.accept(BEDROCK, BEDROCK_MODEL);
        equipmentModelBiConsumer.accept(BEDROCKIUM, BEDROCKIUM_MODEL);
    }

    private static EquipmentModel buildHumanoid(String path) {
        return EquipmentModel.builder().addHumanoidLayers(Identifier.of(ChainMiningReforged.MOD_ID, path)).build();
    }

    static RegistryKey<EquipmentAsset> register(String name) {
        return RegistryKey.of(REGISTRY_KEY, Identifier.of(ChainMiningReforged.MOD_ID,name));
    }
}

