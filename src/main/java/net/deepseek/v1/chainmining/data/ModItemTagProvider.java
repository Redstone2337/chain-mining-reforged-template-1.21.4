package net.deepseek.v1.chainmining.data;

import net.deepseek.v1.chainmining.items.ModItems;
import net.deepseek.v1.chainmining.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {


    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModItemTags.FUNCTION_LIST)
                .add(Items.DIAMOND_AXE)
                .add(Items.NETHERITE_AXE)
                .add(Items.GOLDEN_AXE)
                .add(Items.IRON_AXE)
                .add(Items.STONE_AXE)
                .add(Items.WOODEN_AXE);
        getOrCreateTagBuilder(ModItemTags.PICKAXE)
                .add(Items.DIAMOND_PICKAXE)
                .add(Items.NETHERITE_PICKAXE)
                .add(Items.GOLDEN_PICKAXE)
                .add(Items.IRON_PICKAXE)
                .add(Items.STONE_PICKAXE)
                .add(Items.WOODEN_PICKAXE);
        getOrCreateTagBuilder(ModItemTags.SWORD)
                .add(Items.DIAMOND_SWORD)
                .add(Items.NETHERITE_SWORD)
                .add(Items.GOLDEN_SWORD)
                .add(Items.IRON_SWORD)
                .add(Items.STONE_SWORD)
                .add(Items.WOODEN_SWORD);
        getOrCreateTagBuilder(ModItemTags.ALL)
                .addTags(
                        ModItemTags.SWORD,
                        ModItemTags.PICKAXE,
                        ModItemTags.FUNCTION_LIST
                );
        getOrCreateTagBuilder(ModItemTags.BEDROCK_TOOL_MATERIALS)
                .add(ModItems.BEDROCK_STONE);

        getOrCreateTagBuilder(ModItemTags.BEDROCKIUM_TOOL_MATERIALS)
                .add(ModItems.BEDROCKIUM_STONE);
        getOrCreateTagBuilder(ModItemTags.REPAIRS_BEDROCK_ARMOR)
                .add(ModItems.BEDROCK_STONE);
        getOrCreateTagBuilder(ModItemTags.REPAIRS_BEDROCKIUM_ARMOR)
                .add(ModItems.BEDROCKIUM_STONE);
        getOrCreateTagBuilder(ItemTags.ARMOR_ENCHANTABLE)
                .add(
                        ModItems.BEDROCK_HELMET,
                        ModItems.BEDROCK_CHESTPLATE,
                        ModItems.BEDROCK_LEGGINGS,
                        ModItems.BEDROCK_BOOTS,
                        ModItems.BEDROCKIUM_HELMET,
                        ModItems.BEDROCKIUM_CHESTPLATE,
                        ModItems.BEDROCKIUM_LEGGINGS,
                        ModItems.BEDROCKIUM_BOOTS
                );
        getOrCreateTagBuilder(ItemTags.WEAPON_ENCHANTABLE)
                .add(
                        ModItems.BEDROCK_AXE,
                        ModItems.BEDROCKIUM_AXE,
                        ModItems.BEDROCK_SWORD,
                        ModItems.BEDROCKIUM_SWORD
                );
        getOrCreateTagBuilder(ItemTags.MINING_ENCHANTABLE)
                .add(
                        ModItems.BEDROCK_PICKAXE,
                        ModItems.BEDROCKIUM_PICKAXE,
                        ModItems.BEDROCK_SHOVEL,
                        ModItems.BEDROCKIUM_SHOVEL,
                        ModItems.BEDROCK_AXE,
                        ModItems.BEDROCKIUM_AXE,
                        ModItems.BEDROCK_HOE,
                        ModItems.BEDROCKIUM_HOE
                );
    }
}
