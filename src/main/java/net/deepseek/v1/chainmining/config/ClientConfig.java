package net.deepseek.v1.chainmining.config;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue IS_CLEAR_SERVER_ITEM = BUILDER
            .comment("开启服务器掉落物清理功能")
            .define("ClearClientSettings.isClearServerItem", false);

    public static final ModConfigSpec.ConfigValue<String> DISPLAY_TEXT_HEAD = BUILDER
            .comment("清理时候显示的文本头\n文本格式：文本头+文本体")
            .define("ClearClientSettings.displayTextHead", "[扫地姬]");

    public static final ModConfigSpec.ConfigValue<String> DISPLAY_TEXT_BODY = BUILDER
            .comment("清理的时候显示的文本体\n文本格式：文本头+文本体")
            .define("ClearClientSettings.displayTextBody", "本次总共清理了%s种掉落物，距离下次清理还剩%s秒");

    public static final ModConfigSpec.ConfigValue<String> DISPLAY_COUNTDOWN_TEXT = BUILDER
            .comment("清理的时候显示的文本体\n文本格式：文本头+倒计时提醒")
            .define("ClearClientSettings.displayCountdownText", "亲爱的冒险家们，本次清理即将开始，剩余时间%s秒");

    public static final ModConfigSpec.IntValue CLEAR_TIME = BUILDER
            .comment("清理的时间(单位:游戏刻)\n清理时间的范围在180t ~ 36000t以内\n超过范围或者范围，越界则不执行，默认5分钟。")
            .defineInRange("ServerItemSettings.clearTime", 6000, 180, 36000);

    public static final ModConfigSpec.IntValue CLEAR_CHUNK_RADIUS = BUILDER
            .comment("清理的区块范围半径\n超过最大范围，或者越界半径，则不执行，默认范围是玩家坐标的两格内")
            .defineInRange("ServerItemSettings.clearChunkRadius", 2, 1, 128);


    public static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateString(final Object obj) {
        return obj instanceof String;
    }

    // 初始化配置
    public static void init() {
        NeoForgeConfigRegistry.INSTANCE.register(ChainMiningReforged.MOD_ID, ModConfig.Type.CLIENT, SPEC, "tam_config.toml");
    }

    public static String getDisplayTextHead() {
        return DISPLAY_TEXT_HEAD.get();
    }

    public static String getDisplayTextBody() {
        return DISPLAY_TEXT_BODY.get();
    }

    public static String getDisplayCountdownText() {
        return DISPLAY_COUNTDOWN_TEXT.get();
    }

    public static int getClearTime() {
        return CLEAR_TIME.get();
    }

    public static boolean isClearServerItem() {
        return IS_CLEAR_SERVER_ITEM.get();
    }

    public static int getClearChunkRadius() {
        return CLEAR_CHUNK_RADIUS.get();
    }


    public static void setClearServerItem(boolean isClearServerItem) {
        IS_CLEAR_SERVER_ITEM.set(isClearServerItem);
    }

    public static void setDisplayTextHead(String displayTextHead) {
        DISPLAY_TEXT_HEAD.set(displayTextHead);
    }

    public static void setDisplayTextBody(String displayTextBody) {
        DISPLAY_TEXT_BODY.set(displayTextBody);
    }

    public static void setDisplayCountdownText(String displayCountdownText) {
        DISPLAY_COUNTDOWN_TEXT.set(displayCountdownText);
    }

    public static void setClearTime(int clearTime) {
        CLEAR_TIME.set(clearTime);
    }

    public static void setClearChunkRadius(int clearChunkRadius) {
        CLEAR_CHUNK_RADIUS.set(clearChunkRadius);
    }
}
