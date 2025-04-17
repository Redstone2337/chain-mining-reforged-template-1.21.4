package net.deepseek.v1.chainmining.data;

import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.deepseek.v1.chainmining.enchantments.ModEnchantments;
import net.deepseek.v1.chainmining.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.addEnchantment(ModEnchantments.FIRE_FOR_ONESELF,"Fire One Oneself");
        translationBuilder.addEnchantment(ModEnchantments.ONE_PUSH,"One Push");
        translationBuilder.addEnchantment(ModEnchantments.ICE_BLEND,"Ice Blend");
        translationBuilder.addEnchantment(ModEnchantments.FIRE_BLEND,"Fire Blend");
        translationBuilder.addEnchantment(ModEnchantments.ICE_FIRE_BLEND,"Ice Fire Blend");
        translationBuilder.add(ModItems.BEDROCK_STONE,"Bedrock Stone");
        translationBuilder.add(ModItems.BEDROCKIUM_STONE,"Bedrockium Stone");
        translationBuilder.add(ModItems.DIAMOND_STICK,"Diamond Stick");
        translationBuilder.add(ModItems.BEDROCK_SWORD,"Bedrock Sword");
        translationBuilder.add(ModItems.BEDROCK_PICKAXE,"Bedrock Pickaxe");
        translationBuilder.add(ModItems.BEDROCK_AXE,"Bedrock Axe");
        translationBuilder.add(ModItems.BEDROCK_SHOVEL,"Bedrock Shovel");
        translationBuilder.add(ModItems.BEDROCK_HOE,"Bedrock Hoe");
        translationBuilder.add(ModItems.BEDROCKIUM_SWORD,"Bedrockium Sword");
        translationBuilder.add(ModItems.BEDROCKIUM_PICKAXE,"Bedrockium Pickaxe");
        translationBuilder.add(ModItems.BEDROCKIUM_AXE,"Bedrockium Axe");
        translationBuilder.add(ModItems.BEDROCKIUM_SHOVEL,"Bedrockium Shovel");
        translationBuilder.add(ModItems.BEDROCKIUM_HOE,"Bedrockium Hoe");
        translationBuilder.add(ModItems.BEDROCK_CHESTPLATE,"Bedrock Chestplate");
        translationBuilder.add(ModItems.BEDROCK_HELMET,"Bedrock Helmet");
        translationBuilder.add(ModItems.BEDROCK_LEGGINGS,"Bedrock Leggings");
        translationBuilder.add(ModItems.BEDROCK_BOOTS,"Bedrock Boots");
        translationBuilder.add(ModItems.BEDROCKIUM_CHESTPLATE,"Bedrockium Chestplate");
        translationBuilder.add(ModItems.BEDROCKIUM_HELMET,"Bedrockium Helmet");
        translationBuilder.add(ModItems.BEDROCKIUM_LEGGINGS,"Bedrockium Leggings");
        translationBuilder.add(ModItems.BEDROCKIUM_BOOTS,"Bedrockium Boots");
        translationBuilder.add(ModBlocks.BEDROCKIUM_ORE,"Bedrockium Ore");
        translationBuilder.add(ModBlocks.TELEPORT_BLOCK,"Teleport Block");
        translationBuilder.add(ModItems.ICE_CLAW,"Ice Claw");
    }
}
