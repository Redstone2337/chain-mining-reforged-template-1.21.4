package net.deepseek.v1.chainmining.blocks;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.event.GeoRenderEvent;

import java.util.function.Function;

public class ModBlocks {

    public static final Block BEDROCKIUM_ORE = register("bedrockium_ore", Block::new, AbstractBlock.Settings.create()
            .mapColor(MapColor.BLUE)
            .instrument(NoteBlockInstrument.HARP)
            .requiresTool()
            .strength(10.0f,15.0f));

    public static final Block TELEPORT_BLOCK = register("teleport_block",Block::new, AbstractBlock.Settings.create()
            .mapColor(MapColor.BLUE)
            .instrument(NoteBlockInstrument.HARP)
            .requiresTool()
            .strength(10.0f,15.0f));

    public static final Block FUNC_TELEPORT_BLOCK = register("func_teleport_block",TeleportBlock::new, AbstractBlock.Settings.create()
            .mapColor(MapColor.BLUE)
            .instrument(NoteBlockInstrument.HARP)
            .requiresTool()
            .strength(10.0f,15.0f));

    private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(ChainMiningReforged.MOD_ID, id));
        final Block block = Blocks.register(registryKey, factory, settings);
        Items.register(block);
        return block;
    }

    private static Block register(String id, AbstractBlock.Settings settings) {
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(ChainMiningReforged.MOD_ID, id));
        final Block block = Blocks.register(registryKey, Block::new, settings);
        Items.register(block);
        return block;
    }

    public static void register() {
        ChainMiningReforged.LOGGER.info("ModBlocks: Registering blocks for " + ChainMiningReforged.MOD_ID);
    }
}
