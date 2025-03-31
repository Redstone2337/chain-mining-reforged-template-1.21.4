package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.deepseek.v1.chainmining.core.data.PlayerSelectionData;
import net.deepseek.v1.chainmining.util.SelectionArea;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;

import java.util.Objects;
import java.util.function.Supplier;

public class TestCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                CommandManager.literal("//test")
                        .then(CommandManager.argument("targets", BlockStateArgumentType.blockState(registryAccess))
                                .then(CommandManager.literal("run")
                                        .then(CommandManager.argument("command", StringArgumentType.greedyString())
                                                .executes(ctx -> executeTest(
                                                        ctx.getSource(),
                                                        BlockStateArgumentType.getBlockState(ctx, "targets"),
                                                        StringArgumentType.getString(ctx, "command")
                                                )))
                                        .executes(ctx -> executeTest(
                                                ctx.getSource(),
                                                BlockStateArgumentType.getBlockState(ctx, "targets"),
                                                null
                                        )))
                        ));
    }

    private static int executeTest(ServerCommandSource source, BlockStateArgument target, String command) {
        PlayerSelectionData data = PlayerSelectionData.get(Objects.requireNonNull(source.getPlayer()));
        SelectionArea area = data.getSelection();

        if (area == null || !area.isValid()) {
            source.sendError(Text.literal("无效选区或选区过大"));
            return 0;
        }

        if (command != null) {
            source.getServer().getCommandManager().executeWithPrefix(source, command);
            return 1;
        } else {
                    int[] result = {0, 0}; // {totalPower, poweredBlocks}
            area.forEachBlock(source.getWorld(), pos -> {
                int power = source.getWorld().getEmittedRedstonePower(pos, Direction.UP);
                if (power > 0) {
                    result[0] += power;
                    result[1]++;
                }
            });
            source.sendFeedback((Supplier<Text>) Text.literal(
                    String.format("红石信号: %d 方块, 强度: %d", result[1], result[0])
            ), false);
            return result[0];
        }
    }
}



