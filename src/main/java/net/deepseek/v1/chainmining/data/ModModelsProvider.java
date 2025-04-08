package net.deepseek.v1.chainmining.data;

import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.deepseek.v1.chainmining.core.armor.ModEquipmentAssetKeys;
import net.deepseek.v1.chainmining.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

public class ModModelsProvider extends FabricModelProvider {
    public ModModelsProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
//        blockStateModelGenerator.registerSimpleState(ModBlocks.BEDROCKIUM_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BEDROCKIUM_ORE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.BEDROCK_STONE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BEDROCKIUM_STONE, Models.GENERATED);
        itemModelGenerator.register(ModItems.DIAMOND_STICK, Models.GENERATED);

        itemModelGenerator.register(ModItems.BEDROCK_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BEDROCK_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BEDROCK_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BEDROCK_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BEDROCK_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BEDROCKIUM_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BEDROCKIUM_PICKAXE, Models.HANDHELD);

        itemModelGenerator.registerArmor(ModItems.BEDROCK_HELMET, ModEquipmentAssetKeys.BEDROCK_ASSET,"bedrock_helmet",true);
        itemModelGenerator.registerArmor(ModItems.BEDROCK_CHESTPLATE, ModEquipmentAssetKeys.BEDROCK_ASSET,"bedrock_chestplate",true);
        itemModelGenerator.registerArmor(ModItems.BEDROCK_LEGGINGS, ModEquipmentAssetKeys.BEDROCK_ASSET,"bedrock_leggings",true);
        itemModelGenerator.registerArmor(ModItems.BEDROCK_BOOTS, ModEquipmentAssetKeys.BEDROCK_ASSET,"bedrock_boots",true);

        itemModelGenerator.registerArmor(ModItems.BEDROCKIUM_HELMET, ModEquipmentAssetKeys.BEDROCKIUM_ASSET,"bedrockium_helmet",true);
        itemModelGenerator.registerArmor(ModItems.BEDROCKIUM_CHESTPLATE, ModEquipmentAssetKeys.BEDROCKIUM_ASSET,"bedrockium_chestplate",true);
        itemModelGenerator.registerArmor(ModItems.BEDROCKIUM_LEGGINGS, ModEquipmentAssetKeys.BEDROCKIUM_ASSET,"bedrockium_leggings",true);
        itemModelGenerator.registerArmor(ModItems.BEDROCKIUM_BOOTS, ModEquipmentAssetKeys.BEDROCKIUM_ASSET,"bedrockium_boots",true);

        itemModelGenerator.registerBow(ModItems.BEDROCKIUM_BOW);
        itemModelGenerator.registerArmor(ModItems.BEDROCKIUM_ARROW, ModEquipmentAssetKeys.BEDROCKIUM_ARROW,"bedrockium_arrow",true);
        itemModelGenerator.registerTippedArrow(ModItems.TRPPED_BEDROCKIUM_ARROW);
    }
}
