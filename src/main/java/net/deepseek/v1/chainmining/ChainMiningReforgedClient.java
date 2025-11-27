package net.deepseek.v1.chainmining;

import net.deepseek.v1.chainmining.config.CommonConfig;
import net.deepseek.v1.chainmining.core.keys.ModKeys;
import net.deepseek.v1.chainmining.event.BlockBreakHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import static net.deepseek.v1.chainmining.core.keys.ModKeys.*;

public class ChainMiningReforgedClient implements ClientModInitializer {
    private static boolean isWaitingForConfirmation = false;

    @Override
    public void onInitializeClient() {
        ModKeys.init();

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            while (ModKeys.getChainMiningReforgedKey().wasPressed()) {
                if (minecraftClient.player != null) {
                    BlockBreakHandler.triggerChainMining(minecraftClient.player);
                }
            }

            if (minecraftClient.player == null) return;

            while (getSpawnKeyBinding().wasPressed()) {
                if (!isWaitingForConfirmation) {
                    // 第一次按下shift
                    minecraftClient.player.sendMessage(Text.of("是否要生成10~20只小蝌蚪? (再次按下Shift确认)"), false);
                    isWaitingForConfirmation = true;
                } else {
                    // 第二次按下shift确认生成
                    spawnTadpoles(minecraftClient.player);
                    isWaitingForConfirmation = false;
                }
            }
        });
    }

    private void spawnTadpoles(PlayerEntity player) {
        int count = Math.min(CommonConfig.getSpawnNormalCount(), CommonConfig.getSpawnMaxCount());
        count = Math.min(count, 100);

        World world = player.getWorld();
        BlockPos playerPos = player.getBlockPos();

        for (int i = 0; i < count; i++) {
            double x = playerPos.getX() + (world.random.nextDouble() - 0.5) * 10;
            double z = playerPos.getZ() + (world.random.nextDouble() - 0.5) * 10;

            // 获取地面高度（最高非空气方块的位置）
            int y = world.getTopY(Heightmap.Type.WORLD_SURFACE, (int)x, (int)z);
            BlockPos spawnPos = new BlockPos((int)x, y, (int)z);

            TadpoleEntity tadpole = EntityType.TADPOLE.create(world,null);
            if (tadpole != null) {
                tadpole.refreshPositionAndAngles(spawnPos, 0, 0);
                if (world.getBlockState(spawnPos).isAir()) {
                    world.spawnEntity(tadpole);
                }
            }
        }

        player.sendMessage(Text.of("已生成 " + count + " 只小蝌蚪!"), false);
    }
}