package net.deepseek.v1.chainmining.core.config;

import net.deepseek.v1.chainmining.config.CommonConfig;

public class ConfigManager {

    public static void initialize() {
        // 初始化已经在 CommonConfig.init() 中完成
    }

    public static int getMaxPowerDistance() {
        return CommonConfig.getMaxPowerDistance();
    }

    public static int getCurrentPowerDistance() {
        return CommonConfig.getCurrentPowerDistance();
    }

    public static void setMaxPowerDistance(int value) {
        CommonConfig.setMaxPowerDistance(value);
        // 确保当前值不超过新最大值
        int current = CommonConfig.getCurrentPowerDistance();
        if (current > value) {
            CommonConfig.setCurrentPowerDistance(value);
        }
    }

    public static void setPowerDistance(int value) {
        CommonConfig.setCurrentPowerDistance(value);
    }

    // 移除 getConfig() 方法，因为不再需要
    // 移除 getScreen() 方法，因为配置屏幕由 NeoForge 处理
}