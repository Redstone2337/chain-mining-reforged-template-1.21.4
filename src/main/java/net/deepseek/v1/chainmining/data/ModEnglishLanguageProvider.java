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
        // 附魔翻译
        translationBuilder.addEnchantment(ModEnchantments.FIRE_FOR_ONESELF,"Fire One Oneself");
        translationBuilder.addEnchantment(ModEnchantments.ONE_PUSH,"One Push");
        translationBuilder.addEnchantment(ModEnchantments.ICE_BLEND,"Ice Blend");
        translationBuilder.addEnchantment(ModEnchantments.FIRE_BLEND,"Fire Blend");
        translationBuilder.addEnchantment(ModEnchantments.ICE_FIRE_BLEND,"Ice Fire Blend");

        // 物品翻译
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
        translationBuilder.add(ModItems.ICE_CLAW,"Ice Claw");

        // 方块翻译
        translationBuilder.add(ModBlocks.BEDROCKIUM_ORE,"Bedrockium Ore");
        translationBuilder.add(ModBlocks.TELEPORT_BLOCK,"Teleport Block");

        // 配置屏幕翻译
        translationBuilder.add("title.chainmining.config", "Chain Mining Config");
        translationBuilder.add("category.chainmining.common", "Common Settings");
        translationBuilder.add("category.chainmining.client", "Client Settings");

        translationBuilder.add("option.chainmining.enabled", "Enable Chain Mining");
        translationBuilder.add("option.chainmining.maxChainCount", "Max Chain Count");
        translationBuilder.add("option.chainmining.durabilityMultiplier", "Durability Multiplier");
        translationBuilder.add("option.chainmining.useBlockWhitelist", "Use Whitelist Mode");
        translationBuilder.add("option.chainmining.defaultAllowedBlocks", "Default Allowed Blocks");
        translationBuilder.add("option.chainmining.defaultDeniedBlocks", "Default Denied Blocks");
        translationBuilder.add("option.chainmining.customAllowedBlocks", "Custom Allowed Blocks");
        translationBuilder.add("option.chainmining.customDeniedBlocks", "Custom Denied Blocks");
        translationBuilder.add("option.chainmining.allowedTools", "Allowed Tools");
        translationBuilder.add("option.chainmining.maxPowerDistance", "Max Power Distance");
        translationBuilder.add("option.chainmining.currentPowerDistance", "Current Power Distance");
        translationBuilder.add("option.chainmining.spawnMaxCount", "Max Spawn Count");
        translationBuilder.add("option.chainmining.spawnNormalCount", "Normal Spawn Count");
        translationBuilder.add("option.chainmining.isClearServerItem", "Enable Item Clear");
        translationBuilder.add("option.chainmining.clearTime", "Clear Interval");
        translationBuilder.add("option.chainmining.clearChunkRadius", "Clear Chunk Radius");
        translationBuilder.add("option.chainmining.displayTextHead", "Display Text Header");
        translationBuilder.add("option.chainmining.displayTextBody", "Display Text Body");
        translationBuilder.add("option.chainmining.displayCountdownText", "Countdown Text");

        translationBuilder.add("tooltip.chainmining.enabled", "Enable or disable chain mining functionality");
        translationBuilder.add("tooltip.chainmining.maxChainCount", "Maximum number of blocks to mine in one chain (0 = unlimited)");
        translationBuilder.add("tooltip.chainmining.durabilityMultiplier", "Durability cost multiplier for each chained block");
        translationBuilder.add("tooltip.chainmining.useBlockWhitelist", "true=whitelist mode, false=blacklist mode");
        translationBuilder.add("tooltip.chainmining.defaultAllowedBlocks", "Default allowed blocks in whitelist mode");
        translationBuilder.add("tooltip.chainmining.defaultDeniedBlocks", "Default denied blocks in blacklist mode");
        translationBuilder.add("tooltip.chainmining.customAllowedBlocks", "User-defined allowed blocks list");
        translationBuilder.add("tooltip.chainmining.customDeniedBlocks", "User-defined denied blocks list");
        translationBuilder.add("tooltip.chainmining.allowedTools", "Tool types that can use chain mining");
        translationBuilder.add("tooltip.chainmining.maxPowerDistance", "Maximum redstone signal transmission distance");
        translationBuilder.add("tooltip.chainmining.currentPowerDistance", "Current redstone signal transmission distance");
        translationBuilder.add("tooltip.chainmining.spawnMaxCount", "Maximum entity spawn count");
        translationBuilder.add("tooltip.chainmining.spawnNormalCount", "Default entity spawn count");
        translationBuilder.add("tooltip.chainmining.isClearServerItem", "Enable server item clearing functionality");
        translationBuilder.add("tooltip.chainmining.clearTime", "Clear interval in game ticks");
        translationBuilder.add("tooltip.chainmining.clearChunkRadius", "Clear radius in chunks");
        translationBuilder.add("tooltip.chainmining.displayTextHead", "Text header displayed during clearing");
        translationBuilder.add("tooltip.chainmining.displayTextBody", "Text body displayed during clearing (%s will be replaced)");
        translationBuilder.add("tooltip.chainmining.displayCountdownText", "Countdown text displayed before clearing (%s will be replaced)");
    }
}