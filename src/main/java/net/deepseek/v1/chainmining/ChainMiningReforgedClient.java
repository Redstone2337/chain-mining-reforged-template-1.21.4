package net.deepseek.v1.chainmining;

import net.deepseek.v1.chainmining.config.ModConfig;
import net.deepseek.v1.chainmining.core.keys.ModKeys;
import net.deepseek.v1.chainmining.event.BlockBreakHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ChainMiningReforgedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModKeys.init();


        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            while (ModKeys.getChainMiningReforgedKey().wasPressed()) {
                if (minecraftClient.player != null) {
                    BlockBreakHandler.triggerChainMining(minecraftClient.player);
                }
            }
        });
    }
}
