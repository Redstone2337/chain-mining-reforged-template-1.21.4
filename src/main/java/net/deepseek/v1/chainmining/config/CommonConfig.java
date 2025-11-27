package net.deepseek.v1.chainmining.config;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

public class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLED = BUILDER
            .comment("是否启用连锁采集")
            .define("ChainMiningSettings.enabled", true);

    public static final ModConfigSpec.IntValue MAX_CHAIN_COUNT = BUILDER
            .comment("最大连锁数量（0为无限制）")
            .defineInRange("ChainMiningSettings.maxChainCount", 10, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue DURABILITY_MULTIPLIER = BUILDER
            .comment("每次连锁消耗的耐久倍数")
            .defineInRange("ChainMiningSettings.durabilityMultiplier", 1.0, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> ALLOWED_TOOLS = BUILDER
            .comment("允许使用的工具类型 [pickaxe, axe, shovel, hoe]")
            .defineListAllowEmpty("ChainMiningSettings.allowedTools",
                    Arrays.asList("pickaxe", "axe", "shovel"),
                    () -> "",
                    CommonConfig::validateString);

    public static final ModConfigSpec.BooleanValue USE_BLOCK_WHITELIST = BUILDER
            .comment("方块过滤模式：true=白名单，false=黑名单")
            .define("ChainMiningSettings.useBlockWhitelist", true);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> DEFAULT_ALLOWED_BLOCKS = BUILDER
            .comment("默认允许连锁的方块列表（白名单模式）")
            .defineListAllowEmpty("ChainMiningSettings.defaultAllowedBlocks",
                    Arrays.asList(
                            "minecraft:oak_log",
                            "minecraft:stone",
                            "minecraft:dirt",
                            "minecraft:coal_ore"
                    ),
                    () -> "",
                    CommonConfig::validateString);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> DEFAULT_DENIED_BLOCKS = BUILDER
            .comment("默认禁止连锁的方块列表（黑名单模式）")
            .defineListAllowEmpty("ChainMiningSettings.defaultDeniedBlocks",
                    Arrays.asList(
                            "minecraft:bedrock",
                            "minecraft:spawner",
                            "minecraft:air"
                    ),
                    () -> "",
                    CommonConfig::validateString);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> CUSTOM_ALLOWED_BLOCKS = BUILDER
            .comment("自定义允许连锁的方块列表（白名单模式）")
            .defineListAllowEmpty("ChainMiningSettings.customAllowedBlocks",
                    List.of(),
                    () -> "",
                    CommonConfig::validateString);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> CUSTOM_DENIED_BLOCKS = BUILDER
            .comment("自定义禁止连锁的方块列表（黑名单模式）")
            .defineListAllowEmpty("ChainMiningSettings.customDeniedBlocks",
                    List.of(),
                    () -> "",
                    CommonConfig::validateString);

    public static final ModConfigSpec.IntValue MAX_POWER_DISTANCE = BUILDER
            .comment("最大信号传输距离")
            .defineInRange("ChainMiningSettings.maxPowerDistance", 32, 1, 128);

    public static final ModConfigSpec.IntValue CURRENT_POWER_DISTANCE = BUILDER
            .comment("当前信号传输距离")
            .defineInRange("ChainMiningSettings.currentPowerDistance", 15, 1, 128);

    public static final ModConfigSpec.IntValue SPAWN_MAX_COUNT = BUILDER
            .comment("最大生成数量")
            .defineInRange("ChainMiningSettings.spawnMaxCount", 20, 1, 100);

    public static final ModConfigSpec.IntValue SPAWN_NORMAL_COUNT = BUILDER
            .comment("默认生成数量")
            .defineInRange("ChainMiningSettings.spawnNormalCount", 10, 1, 100);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateString(final Object obj) {
        return obj instanceof String;
    }

    // 初始化配置
    public static void init() {
        NeoForgeConfigRegistry.INSTANCE.register(ChainMiningReforged.MOD_ID, ModConfig.Type.COMMON, SPEC, "chainmining_config.toml");
    }

    // Getter 方法
    public static boolean isEnabled() {
        return ENABLED.get();
    }

    public static int getMaxChainCount() {
        return MAX_CHAIN_COUNT.get();
    }

    public static double getDurabilityMultiplier() {
        return DURABILITY_MULTIPLIER.get();
    }

    public static List<? extends String> getAllowedTools() {
        return ALLOWED_TOOLS.get();
    }

    public static boolean isUseBlockWhitelist() {
        return USE_BLOCK_WHITELIST.get();
    }

    public static List<? extends String> getDefaultAllowedBlocks() {
        return DEFAULT_ALLOWED_BLOCKS.get();
    }

    public static List<? extends String> getDefaultDeniedBlocks() {
        return DEFAULT_DENIED_BLOCKS.get();
    }

    public static List<? extends String> getCustomAllowedBlocks() {
        return CUSTOM_ALLOWED_BLOCKS.get();
    }

    public static List<? extends String> getCustomDeniedBlocks() {
        return CUSTOM_DENIED_BLOCKS.get();
    }

    public static int getMaxPowerDistance() {
        return MAX_POWER_DISTANCE.get();
    }

    public static int getCurrentPowerDistance() {
        return CURRENT_POWER_DISTANCE.get();
    }

    public static int getSpawnMaxCount() {
        return SPAWN_MAX_COUNT.get();
    }

    public static int getSpawnNormalCount() {
        return SPAWN_NORMAL_COUNT.get();
    }

    // Setter 方法
    public static void setEnabled(boolean enabled) {
        ENABLED.set(enabled);
    }

    public static void setMaxChainCount(int maxChainCount) {
        MAX_CHAIN_COUNT.set(maxChainCount);
    }

    public static void setDurabilityMultiplier(double durabilityMultiplier) {
        DURABILITY_MULTIPLIER.set(durabilityMultiplier);
    }

    public static void setAllowedTools(List<String> allowedTools) {
        ALLOWED_TOOLS.set(allowedTools);
    }

    public static void setUseBlockWhitelist(boolean useBlockWhitelist) {
        USE_BLOCK_WHITELIST.set(useBlockWhitelist);
    }

    public static void setDefaultAllowedBlocks(List<String> defaultAllowedBlocks) {
        DEFAULT_ALLOWED_BLOCKS.set(defaultAllowedBlocks);
    }

    public static void setDefaultDeniedBlocks(List<String> defaultDeniedBlocks) {
        DEFAULT_DENIED_BLOCKS.set(defaultDeniedBlocks);
    }

    public static void setCustomAllowedBlocks(List<String> customAllowedBlocks) {
        CUSTOM_ALLOWED_BLOCKS.set(customAllowedBlocks);
    }

    public static void setCustomDeniedBlocks(List<String> customDeniedBlocks) {
        CUSTOM_DENIED_BLOCKS.set(customDeniedBlocks);
    }

    public static void setMaxPowerDistance(int maxPowerDistance) {
        MAX_POWER_DISTANCE.set(maxPowerDistance);
    }

    public static void setCurrentPowerDistance(int currentPowerDistance) {
        CURRENT_POWER_DISTANCE.set(currentPowerDistance);
    }

    public static void setSpawnMaxCount(int spawnMaxCount) {
        SPAWN_MAX_COUNT.set(spawnMaxCount);
    }

    public static void setSpawnNormalCount(int spawnNormalCount) {
        SPAWN_NORMAL_COUNT.set(spawnNormalCount);
    }
}