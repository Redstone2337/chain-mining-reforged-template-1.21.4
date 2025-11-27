package net.deepseek.v1.chainmining.core.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.deepseek.v1.chainmining.config.ClientConfig;
import net.deepseek.v1.chainmining.config.CommonConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigScreen {

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.chainmining.config"))
                .setSavingRunnable(() -> {
                    // 保存所有配置
                    CommonConfig.SPEC.save();
                    ClientConfig.SPEC.save();
                });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // 通用配置分类
        ConfigCategory commonCategory = builder.getOrCreateCategory(Text.translatable("category.chainmining.common"));

        // 连锁采集设置
        commonCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.chainmining.enabled"), CommonConfig.isEnabled())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("tooltip.chainmining.enabled"))
                .setSaveConsumer(CommonConfig::setEnabled)
                .build());

        commonCategory.addEntry(entryBuilder.startIntField(Text.translatable("option.chainmining.maxChainCount"), CommonConfig.getMaxChainCount())
                .setDefaultValue(10)
                .setMin(0)
                .setMax(1000)
                .setTooltip(Text.translatable("tooltip.chainmining.maxChainCount"))
                .setSaveConsumer(CommonConfig::setMaxChainCount)
                .build());

        commonCategory.addEntry(entryBuilder.startFloatField(Text.translatable("option.chainmining.durabilityMultiplier"), (float) CommonConfig.getDurabilityMultiplier())
                .setDefaultValue(1.0f)
                .setMin(0.0f)
                .setMax(10.0f)
                .setTooltip(Text.translatable("tooltip.chainmining.durabilityMultiplier"))
                .setSaveConsumer(CommonConfig::setDurabilityMultiplier)
                .build());

        // 工具设置 - 安全转换
        commonCategory.addEntry(entryBuilder.startStrList(Text.translatable("option.chainmining.allowedTools"),
                        convertToMutableList(CommonConfig.getAllowedTools()))
                .setDefaultValue(Arrays.asList("pickaxe", "axe", "shovel"))
                .setTooltip(Text.translatable("tooltip.chainmining.allowedTools"))
                .setSaveConsumer(CommonConfig::setAllowedTools)
                .build());

        // 方块过滤设置
        commonCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.chainmining.useBlockWhitelist"), CommonConfig.isUseBlockWhitelist())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("tooltip.chainmining.useBlockWhitelist"))
                .setSaveConsumer(CommonConfig::setUseBlockWhitelist)
                .build());

        // 默认允许方块 - 安全转换
        commonCategory.addEntry(entryBuilder.startStrList(Text.translatable("option.chainmining.defaultAllowedBlocks"),
                        convertToMutableList(CommonConfig.getDefaultAllowedBlocks()))
                .setDefaultValue(Arrays.asList(
                        "minecraft:oak_log",
                        "minecraft:stone",
                        "minecraft:dirt",
                        "minecraft:coal_ore"
                ))
                .setTooltip(Text.translatable("tooltip.chainmining.defaultAllowedBlocks"))
                .setSaveConsumer(CommonConfig::setDefaultAllowedBlocks)
                .build());

        // 默认禁止方块 - 安全转换
        commonCategory.addEntry(entryBuilder.startStrList(Text.translatable("option.chainmining.defaultDeniedBlocks"),
                        convertToMutableList(CommonConfig.getDefaultDeniedBlocks()))
                .setDefaultValue(Arrays.asList(
                        "minecraft:bedrock",
                        "minecraft:spawner",
                        "minecraft:air"
                ))
                .setTooltip(Text.translatable("tooltip.chainmining.defaultDeniedBlocks"))
                .setSaveConsumer(CommonConfig::setDefaultDeniedBlocks)
                .build());

        // 自定义允许方块 - 安全转换
        commonCategory.addEntry(entryBuilder.startStrList(Text.translatable("option.chainmining.customAllowedBlocks"),
                        convertToMutableList(CommonConfig.getCustomAllowedBlocks()))
                .setDefaultValue(new ArrayList<>())
                .setTooltip(Text.translatable("tooltip.chainmining.customAllowedBlocks"))
                .setSaveConsumer(CommonConfig::setCustomAllowedBlocks)
                .build());

        // 自定义禁止方块 - 安全转换
        commonCategory.addEntry(entryBuilder.startStrList(Text.translatable("option.chainmining.customDeniedBlocks"),
                        convertToMutableList(CommonConfig.getCustomDeniedBlocks()))
                .setDefaultValue(new ArrayList<>())
                .setTooltip(Text.translatable("tooltip.chainmining.customDeniedBlocks"))
                .setSaveConsumer(CommonConfig::setCustomDeniedBlocks)
                .build());

        // 红石能量设置
        commonCategory.addEntry(entryBuilder.startIntSlider(Text.translatable("option.chainmining.maxPowerDistance"), CommonConfig.getMaxPowerDistance(), 1, 128)
                .setDefaultValue(32)
                .setTooltip(Text.translatable("tooltip.chainmining.maxPowerDistance"))
                .setSaveConsumer(CommonConfig::setMaxPowerDistance)
                .build());

        commonCategory.addEntry(entryBuilder.startIntSlider(Text.translatable("option.chainmining.currentPowerDistance"), CommonConfig.getCurrentPowerDistance(), 1, 128)
                .setDefaultValue(15)
                .setTooltip(Text.translatable("tooltip.chainmining.currentPowerDistance"))
                .setSaveConsumer(CommonConfig::setCurrentPowerDistance)
                .build());

        // 生成设置
        commonCategory.addEntry(entryBuilder.startIntSlider(Text.translatable("option.chainmining.spawnMaxCount"), CommonConfig.getSpawnMaxCount(), 1, 100)
                .setDefaultValue(20)
                .setTooltip(Text.translatable("tooltip.chainmining.spawnMaxCount"))
                .setSaveConsumer(CommonConfig::setSpawnMaxCount)
                .build());

        commonCategory.addEntry(entryBuilder.startIntSlider(Text.translatable("option.chainmining.spawnNormalCount"), CommonConfig.getSpawnNormalCount(), 1, 100)
                .setDefaultValue(10)
                .setTooltip(Text.translatable("tooltip.chainmining.spawnNormalCount"))
                .setSaveConsumer(CommonConfig::setSpawnNormalCount)
                .build());

        // 客户端配置分类
        ConfigCategory clientCategory = builder.getOrCreateCategory(Text.translatable("category.chainmining.client"));

        // 清理设置
        clientCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.chainmining.isClearServerItem"), ClientConfig.isClearServerItem())
                .setDefaultValue(false)
                .setTooltip(Text.translatable("tooltip.chainmining.isClearServerItem"))
                .setSaveConsumer(ClientConfig::setClearServerItem)
                .build());

        clientCategory.addEntry(entryBuilder.startIntField(Text.translatable("option.chainmining.clearTime"), ClientConfig.getClearTime())
                .setDefaultValue(6000)
                .setMin(180)
                .setMax(36000)
                .setTooltip(Text.translatable("tooltip.chainmining.clearTime"))
                .setSaveConsumer(ClientConfig::setClearTime)
                .build());

        clientCategory.addEntry(entryBuilder.startIntField(Text.translatable("option.chainmining.clearChunkRadius"), ClientConfig.getClearChunkRadius())
                .setDefaultValue(2)
                .setMin(1)
                .setMax(128)
                .setTooltip(Text.translatable("tooltip.chainmining.clearChunkRadius"))
                .setSaveConsumer(ClientConfig::setClearChunkRadius)
                .build());

        // 显示设置
        clientCategory.addEntry(entryBuilder.startStrField(Text.translatable("option.chainmining.displayTextHead"), ClientConfig.getDisplayTextHead())
                .setDefaultValue("[扫地姬]")
                .setTooltip(Text.translatable("tooltip.chainmining.displayTextHead"))
                .setSaveConsumer(ClientConfig::setDisplayTextHead)
                .build());

        clientCategory.addEntry(entryBuilder.startStrField(Text.translatable("option.chainmining.displayTextBody"), ClientConfig.getDisplayTextBody())
                .setDefaultValue("本次总共清理了%s种掉落物，距离下次清理还剩%s秒")
                .setTooltip(Text.translatable("tooltip.chainmining.displayTextBody"))
                .setSaveConsumer(ClientConfig::setDisplayTextBody)
                .build());

        clientCategory.addEntry(entryBuilder.startStrField(Text.translatable("option.chainmining.displayCountdownText"), ClientConfig.getDisplayCountdownText())
                .setDefaultValue("亲爱的冒险家们，本次清理即将开始，剩余时间%s秒")
                .setTooltip(Text.translatable("tooltip.chainmining.displayCountdownText"))
                .setSaveConsumer(ClientConfig::setDisplayCountdownText)
                .build());

        return builder.build();
    }

    /**
     * 安全地将 List<? extends String> 转换为可变的 List<String>
     * 通过创建新的 ArrayList 来避免类型安全问题
     */
    private static List<String> convertToMutableList(List<? extends String> sourceList) {
        if (sourceList == null) {
            return new ArrayList<>();
        }
        return sourceList.stream()
                .map(String::valueOf) // 确保每个元素都是 String
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 替代方案：使用简单的 ArrayList 构造函数
     * 这种方法更简洁，但在某些情况下可能会有警告
     */
    private static List<String> convertToMutableListSimple(List<? extends String> sourceList) {
        if (sourceList == null) {
            return new ArrayList<>();
        }
        // 由于我们知道所有元素都是 String，所以这是安全的
        return new ArrayList<>(sourceList);
    }
}