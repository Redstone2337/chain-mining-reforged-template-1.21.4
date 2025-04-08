package net.deepseek.v1.chainmining.items.groups;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.deepseek.v1.chainmining.items.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

//    public static final RegistryKey<ItemGroup> CHAIN_MINING_REFORGED_GROUP = register("chain_mining_reforged");
    public static final RegistryKey<ItemGroup> BEDROCK_GROUP = register("bedrock");
    public static final RegistryKey<ItemGroup> BEDROCKIUM_GROUP = register("bedrockium");

    private static RegistryKey<ItemGroup> register(String id) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(ChainMiningReforged.MOD_ID,id));
    }

    public static void register() {
        Registry.register(Registries.ITEM_GROUP,BEDROCK_GROUP,
                ItemGroup.create(ItemGroup.Row.TOP,0)
                        .displayName(Text.translatable("itemGroup.cmr.bedrock"))
                        .icon(() -> new ItemStack(ModItems.BEDROCK_STONE)).entries((displayContext, entries) -> {
                            entries.add(ModItems.BEDROCK_STONE);
                            entries.add(ModItems.BEDROCK_SWORD);
                            entries.add(ModItems.BEDROCK_PICKAXE);
                            entries.add(ModItems.BEDROCK_AXE);
                            entries.add(ModItems.BEDROCK_SHOVEL);
                            entries.add(ModItems.BEDROCK_HOE);
                            entries.add(ModItems.BEDROCK_HELMET);
                            entries.add(ModItems.BEDROCK_CHESTPLATE);
                            entries.add(ModItems.BEDROCK_LEGGINGS);
                            entries.add(ModItems.BEDROCK_BOOTS);
                            entries.add(ModItems.DIAMOND_STICK);
//                            entries.add(ModItems.VAMPIRE_TEMPLATE);
//                            entries.add(ModItems.SLOWNESS_TEMPLATE);
                        }).build());

        Registry.register(Registries.ITEM_GROUP,BEDROCKIUM_GROUP,
                ItemGroup.create(ItemGroup.Row.TOP,0)
                        .displayName(Text.translatable("itemGroup.cmr.bedrockium"))
                        .icon(() -> new ItemStack(ModItems.BEDROCKIUM_STONE)).entries((displayContext, entries) -> {
                            entries.add(ModItems.BEDROCKIUM_SWORD);
                            entries.add(ModItems.BEDROCKIUM_PICKAXE);
                            entries.add(ModItems.BEDROCKIUM_AXE);
                            entries.add(ModItems.BEDROCKIUM_SHOVEL);
                            entries.add(ModItems.BEDROCKIUM_HOE);
                            entries.add(ModItems.BEDROCKIUM_STONE);
                            entries.add(ModItems.BEDROCKIUM_HELMET);
                            entries.add(ModItems.BEDROCKIUM_CHESTPLATE);
                            entries.add(ModItems.BEDROCKIUM_LEGGINGS);
                            entries.add(ModItems.BEDROCKIUM_BOOTS);
                            entries.add(ModItems.DIAMOND_STICK);
                            entries.add(ModBlocks.BEDROCKIUM_ORE);
                            entries.add(ModItems.BEDROCKIUM_ARROW);
                            entries.add(ModItems.BEDROCKIUM_BOW);
                            entries.add(ModItems.TRPPED_BEDROCKIUM_ARROW);
                        }).build());
    }
}
