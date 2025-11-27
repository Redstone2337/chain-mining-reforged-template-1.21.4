package net.deepseek.v1.chainmining.data;

import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.deepseek.v1.chainmining.enchantments.ModEnchantments;
import net.deepseek.v1.chainmining.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModChineseLanguageProvider extends FabricLanguageProvider {
    public ModChineseLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        // 附魔翻译
        translationBuilder.addEnchantment(ModEnchantments.FIRE_FOR_ONESELF,"自身燃烧");
        translationBuilder.addEnchantment(ModEnchantments.ONE_PUSH,"一击必杀");
        translationBuilder.addEnchantment(ModEnchantments.ICE_BLEND,"冰霜融合");
        translationBuilder.addEnchantment(ModEnchantments.FIRE_BLEND,"火焰融合");
        translationBuilder.addEnchantment(ModEnchantments.ICE_FIRE_BLEND,"冰火融合");

        // 物品翻译
        translationBuilder.add(ModItems.BEDROCK_STONE,"基岩石");
        translationBuilder.add(ModItems.BEDROCKIUM_STONE,"基岩金属石");
        translationBuilder.add(ModItems.DIAMOND_STICK,"钻石棍");
        translationBuilder.add(ModItems.BEDROCK_SWORD,"基岩剑");
        translationBuilder.add(ModItems.BEDROCK_PICKAXE,"基岩镐");
        translationBuilder.add(ModItems.BEDROCK_AXE,"基岩斧");
        translationBuilder.add(ModItems.BEDROCK_SHOVEL,"基岩锹");
        translationBuilder.add(ModItems.BEDROCK_HOE,"基岩锄");
        translationBuilder.add(ModItems.BEDROCKIUM_SWORD,"基岩金属剑");
        translationBuilder.add(ModItems.BEDROCKIUM_PICKAXE,"基岩金属镐");
        translationBuilder.add(ModItems.BEDROCKIUM_AXE,"基岩金属斧");
        translationBuilder.add(ModItems.BEDROCKIUM_SHOVEL,"基岩金属锹");
        translationBuilder.add(ModItems.BEDROCKIUM_HOE,"基岩金属锄");
        translationBuilder.add(ModItems.BEDROCK_CHESTPLATE,"基岩胸甲");
        translationBuilder.add(ModItems.BEDROCK_HELMET,"基岩头盔");
        translationBuilder.add(ModItems.BEDROCK_LEGGINGS,"基岩护腿");
        translationBuilder.add(ModItems.BEDROCK_BOOTS,"基岩靴子");
        translationBuilder.add(ModItems.BEDROCKIUM_CHESTPLATE,"基岩金属胸甲");
        translationBuilder.add(ModItems.BEDROCKIUM_HELMET,"基岩金属头盔");
        translationBuilder.add(ModItems.BEDROCKIUM_LEGGINGS,"基岩金属护腿");
        translationBuilder.add(ModItems.BEDROCKIUM_BOOTS,"基岩金属靴子");
        translationBuilder.add(ModItems.ICE_CLAW,"冰爪");

        // 方块翻译
        translationBuilder.add(ModBlocks.BEDROCKIUM_ORE,"基岩金属矿石");
        translationBuilder.add(ModBlocks.TELEPORT_BLOCK,"传送方块");

        // 配置屏幕翻译
        translationBuilder.add("title.chainmining.config", "连锁采集配置");
        translationBuilder.add("category.chainmining.common", "通用设置");
        translationBuilder.add("category.chainmining.client", "客户端设置");

        translationBuilder.add("option.chainmining.enabled", "启用连锁采集");
        translationBuilder.add("option.chainmining.maxChainCount", "最大连锁数量");
        translationBuilder.add("option.chainmining.durabilityMultiplier", "耐久消耗倍数");
        translationBuilder.add("option.chainmining.useBlockWhitelist", "使用白名单模式");
        translationBuilder.add("option.chainmining.defaultAllowedBlocks", "默认允许方块");
        translationBuilder.add("option.chainmining.defaultDeniedBlocks", "默认禁止方块");
        translationBuilder.add("option.chainmining.customAllowedBlocks", "自定义允许方块");
        translationBuilder.add("option.chainmining.customDeniedBlocks", "自定义禁止方块");
        translationBuilder.add("option.chainmining.allowedTools", "允许的工具类型");
        translationBuilder.add("option.chainmining.maxPowerDistance", "最大信号距离");
        translationBuilder.add("option.chainmining.currentPowerDistance", "当前信号距离");
        translationBuilder.add("option.chainmining.spawnMaxCount", "最大生成数量");
        translationBuilder.add("option.chainmining.spawnNormalCount", "默认生成数量");
        translationBuilder.add("option.chainmining.isClearServerItem", "启用掉落物清理");
        translationBuilder.add("option.chainmining.clearTime", "清理间隔时间");
        translationBuilder.add("option.chainmining.clearChunkRadius", "清理区块半径");
        translationBuilder.add("option.chainmining.displayTextHead", "显示文本头部");
        translationBuilder.add("option.chainmining.displayTextBody", "显示文本主体");
        translationBuilder.add("option.chainmining.displayCountdownText", "倒计时文本");

        translationBuilder.add("tooltip.chainmining.enabled", "是否启用连锁采集功能");
        translationBuilder.add("tooltip.chainmining.maxChainCount", "一次连锁最多采集的方块数量 (0 = 无限制)");
        translationBuilder.add("tooltip.chainmining.durabilityMultiplier", "每次连锁消耗的耐久倍数");
        translationBuilder.add("tooltip.chainmining.useBlockWhitelist", "true=白名单模式, false=黑名单模式");
        translationBuilder.add("tooltip.chainmining.defaultAllowedBlocks", "白名单模式下默认允许连锁的方块");
        translationBuilder.add("tooltip.chainmining.defaultDeniedBlocks", "黑名单模式下默认禁止连锁的方块");
        translationBuilder.add("tooltip.chainmining.customAllowedBlocks", "用户自定义的允许连锁方块列表");
        translationBuilder.add("tooltip.chainmining.customDeniedBlocks", "用户自定义的禁止连锁方块列表");
        translationBuilder.add("tooltip.chainmining.allowedTools", "允许使用连锁采集的工具类型");
        translationBuilder.add("tooltip.chainmining.maxPowerDistance", "红石信号最大传输距离");
        translationBuilder.add("tooltip.chainmining.currentPowerDistance", "当前红石信号传输距离");
        translationBuilder.add("tooltip.chainmining.spawnMaxCount", "最大生成实体数量");
        translationBuilder.add("tooltip.chainmining.spawnNormalCount", "默认生成实体数量");
        translationBuilder.add("tooltip.chainmining.isClearServerItem", "是否启用服务器掉落物清理功能");
        translationBuilder.add("tooltip.chainmining.clearTime", "清理间隔时间 (游戏刻)");
        translationBuilder.add("tooltip.chainmining.clearChunkRadius", "清理的区块范围半径");
        translationBuilder.add("tooltip.chainmining.displayTextHead", "清理时显示的文本头部");
        translationBuilder.add("tooltip.chainmining.displayTextBody", "清理时显示的文本主体 (%s 会被替换)");
        translationBuilder.add("tooltip.chainmining.displayCountdownText", "清理倒计时显示的文本 (%s 会被替换)");
    }
}