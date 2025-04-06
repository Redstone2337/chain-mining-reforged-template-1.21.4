package net.deepseek.v1.chainmining.data;

import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.deepseek.v1.chainmining.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTablesProvider extends FabricBlockLootTableProvider {
    private final RegistryWrapper.WrapperLookup registryLookup;

    public ModLootTablesProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
        this.registryLookup = registryLookup.getNow(null);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.BEDROCKIUM_ORE,dropMoreSelf(ModBlocks.BEDROCKIUM_ORE, ModItems.BEDROCKIUM_STONE));
    }

    public LootTable.Builder dropMoreSelf(Block block, Item item) {
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(block, (LootPoolEntry.Builder)this.applyExplosionDecay(block,
                        (LeafEntry.Builder) ItemEntry.builder(item)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F))))
                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE))
                )
        );
    }
}
