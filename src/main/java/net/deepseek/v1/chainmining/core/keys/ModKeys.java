package net.deepseek.v1.chainmining.core.keys;

import net.deepseek.v1.chainmining.config.ModConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeys {
    private static KeyBinding ChainMiningReforgedKey;
    private static ModConfig config;

    public static void init() {
        ChainMiningReforgedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chainmining.chain_mining_reforged.name",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "category.chainmining.main.title"
        ));
        config.setKeys(ChainMiningReforgedKey.getTranslationKey());
    }

    public static KeyBinding getChainMiningReforgedKey() {
        return ChainMiningReforgedKey;
    }

    public static void setChainMiningReforgedKey(KeyBinding chainMiningReforgedKey) {
        ChainMiningReforgedKey = chainMiningReforgedKey;
    }
}
