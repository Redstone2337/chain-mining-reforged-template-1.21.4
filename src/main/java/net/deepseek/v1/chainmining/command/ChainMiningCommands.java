package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.deepseek.v1.chainmining.config.CommonConfig;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ChainMiningCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        LiteralCommandNode<ServerCommandSource> chainMiningNode = dispatcher.register(
                CommandManager.literal("chainmining")
                        .requires(src -> src.hasPermissionLevel(2))
                        .then(CommandManager.literal("allow")
                                .executes(run -> setBlockList(
                                                run.getSource(),
                                                true,
                                                null
                                        )
                                )
                                .then(CommandManager.argument("block", BlockStateArgumentType.blockState(commandRegistryAccess))
                                        .executes(run -> setBlockList(
                                                        run.getSource(),
                                                        true,
                                                        BlockStateArgumentType.getBlockState(run,"block")
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("denied")
                                .executes(run -> setBlockList(
                                                run.getSource(),
                                                false,
                                                null
                                        )
                                )
                                .then(CommandManager.argument("block", BlockStateArgumentType.blockState(commandRegistryAccess))
                                        .executes(run -> setBlockList(
                                                        run.getSource(),
                                                        false,
                                                        BlockStateArgumentType.getBlockState(run,"block")
                                                )
                                        )
                                )
                        )
        );
        dispatcher.register(CommandManager.literal("cmr").requires(src -> src.hasPermissionLevel(2)).redirect(chainMiningNode));
    }


    // 指令逻辑
    private static int setBlockList(ServerCommandSource source, boolean allow, BlockStateArgument block) {
        // 获取玩家
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.literal("只有玩家可以使用此指令！"));
            return 0;
        }

        // 获取目标方块
        BlockPos targetPos = null;
        if (block == null) {
            // 获取玩家准心对准的方块
            HitResult hit = player.raycast(5.0, 0.0f, false);
            if (hit.getType() == HitResult.Type.BLOCK) {
                targetPos = ((BlockHitResult) hit).getBlockPos();
            } else {
                source.sendError(Text.literal("请对准一个方块！"));
                return 0;
            }
        }

        // 获取方块 ID
        String blockId;
        if (block != null) {
            blockId = block.getBlockState().getBlock().getName().toString();
        } else {
            blockId = player.getWorld().getBlockState(targetPos).getBlock().getName().toString();
        }

        // 更新配置
        if (allow) {
            List<String> customAllowedBlocks = new ArrayList<>(CommonConfig.getCustomAllowedBlocks());
            if (customAllowedBlocks.contains(blockId)) {
                source.sendError(Text.literal("方块 " + blockId + " 已在自定义允许列表中！"));
            } else {
                customAllowedBlocks.add(blockId);
                CommonConfig.setCustomAllowedBlocks(customAllowedBlocks);
                source.sendFeedback(() -> Text.literal("已将方块 " + blockId + " 添加到自定义允许列表中！"), false);
            }
        } else {
            List<String> customDeniedBlocks = new ArrayList<>(CommonConfig.getCustomDeniedBlocks());
            if (customDeniedBlocks.contains(blockId)) {
                source.sendError(Text.literal("方块 " + blockId + " 已在自定义禁止列表中！"));
            } else {
                customDeniedBlocks.add(blockId);
                CommonConfig.setCustomDeniedBlocks(customDeniedBlocks);
                source.sendFeedback(() -> Text.literal("已将方块 " + blockId + " 添加到自定义禁止列表中！"), false);
            }
        }

        return 1;
    }
}