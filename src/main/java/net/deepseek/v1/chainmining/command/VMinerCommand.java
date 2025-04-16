package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.deepseek.v1.chainmining.core.mod.MiningTask;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class VMinerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("vminer")
                .then(CommandManager.literal("set")
                        .then(CommandManager.literal("start")
                                .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                        .executes(ctx -> setPos(ctx, true))))
                        .then(CommandManager.literal("end")
                                .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                        .executes(ctx -> setPos(ctx, false))))
                        .then(CommandManager.literal("teleport")
                                .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                        .executes(VMinerCommand::setTeleportPos)))
                        .then(CommandManager.literal("mark")
                                .executes(VMinerCommand::markPos))
                        .then(CommandManager.literal("start")
                                .executes(VMinerCommand::startMiner))
                        .then(CommandManager.literal("end")
                                .executes(VMinerCommand::endMiner))
                        .then(CommandManager.literal("pause")
                                .executes(VMinerCommand::pauseMiner))));
    }

    private static int setPos(CommandContext<ServerCommandSource> ctx, boolean isStart) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "pos");
        ServerPlayerEntity player = ctx.getSource().getPlayer();

        if (isStart) {
            ChainMiningReforged.startPos = pos;
            if (player != null) {
                player.sendMessage(Text.literal("设置起始点为: " + pos.toShortString()), false);
            }
        } else {
            ChainMiningReforged.endPos = pos;
            if (player != null) {
                player.sendMessage(Text.literal("设置结束点为: " + pos.toShortString()), false);
            }
        }

        return Command.SINGLE_SUCCESS;
    }

//    private static int setTeleportPos(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
//        BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "pos");
//        ChainMiningReforged.teleportPos = pos;
//        ctx.getSource().sendFeedback(() -> Text.literal("设置传送方块位置为: " + pos.toShortString()), false);
//        return Command.SINGLE_SUCCESS;
//    }

    private static int markPos(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayer();

        if (!ChainMiningReforged.confirmed) {
            if (ChainMiningReforged.startPos == null || ChainMiningReforged.endPos == null) {
                if (player != null) {
                    player.sendMessage(Text.literal("请先设置起始点和结束点!"), false);
                }
                return 0;
            }

            // 显示范围预览
            if (player != null) {
                drawPreview(player.getServerWorld(), ChainMiningReforged.startPos, ChainMiningReforged.endPos);
            }

            if (player != null) {
                player.sendMessage(Text.literal("请确认挖矿范围是否正确。再次使用/mark确认或取消。"), false);
            }
            ChainMiningReforged.confirmed = true;
        } else {
            // 确认操作
            if (player != null) {
                player.sendMessage(Text.literal("挖矿范围已确认!"), false);
            }
            ChainMiningReforged.confirmed = false;
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int startMiner(CommandContext<ServerCommandSource> ctx) {
        if (ChainMiningReforged.isRunning) {
            ctx.getSource().sendFeedback(() -> Text.literal("[VMiner] 挖矿机已经在运行!"), false);
            return 0;
        }

        if (ChainMiningReforged.startPos == null || ChainMiningReforged.endPos == null) {
            ctx.getSource().sendFeedback(() -> Text.literal("[VMiner] 请先设置起始点和结束点并确认!"), false);
            return 0;
        }

        // 计算挖掘范围
        int minX = Math.min(ChainMiningReforged.startPos.getX(), ChainMiningReforged.endPos.getX());
        int maxX = Math.max(ChainMiningReforged.startPos.getX(), ChainMiningReforged.endPos.getX());
        int minZ = Math.min(ChainMiningReforged.startPos.getZ(), ChainMiningReforged.endPos.getZ());
        int maxZ = Math.max(ChainMiningReforged.startPos.getZ(), ChainMiningReforged.endPos.getZ());
        int minY = Math.min(ChainMiningReforged.startPos.getY(), ChainMiningReforged.endPos.getY());
        int maxY = Math.max(ChainMiningReforged.startPos.getY(), ChainMiningReforged.endPos.getY());

        // 计算总方块数量
        int totalBlocks = (maxX - minX + 1) * (maxZ - minZ + 1) * (maxY - minY + 1);

        if (totalBlocks > ChainMiningReforged.MAX_BLOCKS_PER_OPERATION) {
            ctx.getSource().sendFeedback(() -> Text.literal(String.format(
                    "[VMiner] 错误: 挖矿范围太大! 最大允许挖掘 %,d 个方块，当前范围包含 %,d 个方块",
                    ChainMiningReforged.MAX_BLOCKS_PER_OPERATION, totalBlocks)), false);
            return 0;
        }

        ChainMiningReforged.isRunning = true;
        ctx.getSource().sendFeedback(() -> Text.literal(String.format(
                "[VMiner] 垂直挖矿机已启动! 将挖掘 %,d 个方块", totalBlocks)), false);

        // 开始挖矿逻辑
        new MiningTask(ctx.getSource().getServer(),
                ChainMiningReforged.startPos,
                ChainMiningReforged.endPos,
                ChainMiningReforged.teleportPos).start();

        return Command.SINGLE_SUCCESS;
    }

    private static int endMiner(CommandContext<ServerCommandSource> ctx) {
        if (!ChainMiningReforged.isRunning) {
            ctx.getSource().sendFeedback(() -> Text.literal("挖矿机没有在运行!"), false);
            return 0;
        }

        ChainMiningReforged.isRunning = false;
        ctx.getSource().sendFeedback(() -> Text.literal("垂直挖矿机已停止!"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int pauseMiner(CommandContext<ServerCommandSource> ctx) {
        // 暂停逻辑可以在MiningTask中实现
        ctx.getSource().sendFeedback(() -> Text.literal("垂直挖矿机已暂停!"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static void drawPreview(ServerWorld world, BlockPos start, BlockPos end) {
        // 计算范围
        int minX = Math.min(start.getX(), end.getX());
        int maxX = Math.max(start.getX(), end.getX());
        int minZ = Math.min(start.getZ(), end.getZ());
        int maxZ = Math.max(start.getZ(), end.getZ());
        int y = start.getY();

        // 绘制黑色和黄色地毯的范围圈
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (x == minX || x == maxX || z == minZ || z == maxZ) {
                    // 边界使用黄色地毯
                    world.setBlockState(new BlockPos(x, y, z), Blocks.YELLOW_CARPET.getDefaultState());
                } else {
                    // 内部使用黑色地毯
                    world.setBlockState(new BlockPos(x, y, z), Blocks.BLACK_CARPET.getDefaultState());
                }
            }
        }
    }


    private static int setTeleportPos(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "pos");
        ServerWorld world = ctx.getSource().getWorld();

        // 检查是否是传送方块
        if (world.getBlockState(pos).getBlock() != ModBlocks.TELEPORT_BLOCK) {
            ctx.getSource().sendError(Text.literal("该位置不是传送方块!"));
            return 0;
        }

        ChainMiningReforged.teleportPos = pos;
        ctx.getSource().sendFeedback(() -> Text.literal("设置传送方块位置为: " + pos.toShortString()), false);
        return Command.SINGLE_SUCCESS;
    }
}
