package net.deepseek.v1.chainmining.blocks.entities;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<TeleportingBlockEntity> TELEPORTING_BLOCK = create("teleport_block", FabricBlockEntityTypeBuilder.create(TeleportingBlockEntity::new, ModBlocks.TELEPORT_BLOCK).build());

    private static <T extends BlockEntityType<?>> T create(String id, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(ChainMiningReforged.MOD_ID,id),blockEntityType);
    }

    public static void register() {
        ChainMiningReforged.LOGGER.info("Registering Mod Block Entities for " + ChainMiningReforged.MOD_ID);
    }
}