package net.deepseek.v1.chainmining.data;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.deepseek.v1.chainmining.blocks.TeleportBlock;
import net.deepseek.v1.chainmining.core.armor.ModEquipmentAssetKeys;
import net.deepseek.v1.chainmining.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ModModelsProvider extends FabricModelProvider {
    public ModModelsProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
//        blockStateModelGenerator.registerSimpleState(ModBlocks.BEDROCKIUM_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BEDROCKIUM_ORE);
        blockStateModelGenerator.registerSimpleState(ModBlocks.TELEPORT_BLOCK);
//        CustomBlockStateModelGenerator.createVerticalBlockStates(
//                ModBlocks.TELEPORT_BLOCK,
//                Identifier.of(ChainMiningReforged.MOD_ID, "block/teleport_block"),
//                Identifier.of(ChainMiningReforged.MOD_ID, "block/teleport_block")
//        );
    }

//    public static class CustomBlockStateModelGenerator {
//        private static BlockStateSupplier createVerticalBlockStates(Block vertBlock, Identifier vertId, Identifier fullBlockId) {
//            VariantSetting<Boolean> uvlock = VariantSettings.UVLOCK;
//            VariantSetting<VariantSettings.Rotation> yRot = VariantSettings.Y;
//            return VariantsBlockStateSupplier.create(vertBlock).coordinate(BlockStateVariantMap.create(TeleportBlock.FACING,TeleportBlock.SINGLE)
//                    .register(Direction.NORTH, true, BlockStateVariant.create().put(VariantSettings.MODEL, vertId).put(uvlock, true))
//                    .register(Direction.EAST, true, BlockStateVariant.create().put(VariantSettings.MODEL, vertId).put(uvlock, true).put(yRot, VariantSettings.Rotation.R90))
//                    .register(Direction.SOUTH, true, BlockStateVariant.create().put(VariantSettings.MODEL, vertId).put(uvlock, true).put(yRot, VariantSettings.Rotation.R180))
//                    .register(Direction.WEST, true, BlockStateVariant.create().put(VariantSettings.MODEL, vertId).put(uvlock, true).put(yRot, VariantSettings.Rotation.R270))
//                    .register(Direction.NORTH, false, BlockStateVariant.create().put(VariantSettings.MODEL, fullBlockId).put(uvlock, true))
//                    .register(Direction.EAST, false, BlockStateVariant.create().put(VariantSettings.MODEL, fullBlockId).put(uvlock, true))
//                    .register(Direction.SOUTH, false, BlockStateVariant.create().put(VariantSettings.MODEL, fullBlockId).put(uvlock, true))
//                    .register(Direction.WEST, false, BlockStateVariant.create().put(VariantSettings.MODEL, fullBlockId).put(uvlock, true)));
//        }
//    }

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
//        itemModelGenerator.register(ModItems.BEDROCKIUM_ARROW,Models.GENERATED);

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
