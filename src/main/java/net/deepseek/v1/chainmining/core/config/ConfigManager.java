package net.deepseek.v1.chainmining.core.config;

// ConfigManager.java
import me.shedaniel.autoconfig.AutoConfig;
import net.deepseek.v1.chainmining.config.ModConfig;

public class ConfigManager {
    private static ModConfig config;

    public static void initialize() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static int getMaxPowerDistance() {
        return config.maxPowerDistance;
    }

    public static int getCurrentPowerDistance() {
        return config.currentPowerDistance;
    }

    public static void setMaxPowerDistance(int value) {
        config.maxPowerDistance = Math.max(1, value);
        // 确保当前值不超过新最大值
        config.currentPowerDistance = Math.min(config.currentPowerDistance, config.maxPowerDistance);
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }

    public static void setPowerDistance(int value) {
        config.currentPowerDistance = Math.min(value, config.maxPowerDistance);
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}