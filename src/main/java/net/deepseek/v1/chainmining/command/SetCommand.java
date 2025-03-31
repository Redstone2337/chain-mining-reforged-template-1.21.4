package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.CommandDispatcher;
import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.core.data.PlayerSelectionData;
import net.deepseek.v1.chainmining.util.SelectionArea;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.function.Supplier;

public class SetCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                CommandManager.literal("//set")
                        .then(CommandManager.argument("block", BlockStateArgumentType.blockState(registryAccess))
                                .executes(ctx -> executeSet(
                                        ctx.getSource(),
                                        BlockStateArgumentType.getBlockState(ctx, "block")
                                )))
        );
    }

    private static int executeSet(ServerCommandSource source, BlockStateArgument blockState) {
        PlayerSelectionData data = PlayerSelectionData.get(Objects.requireNonNull(source.getPlayer()));
        SelectionArea area = data.getSelection();

        if (area == null || !area.isValid()) {
            source.sendError(Text.literal("无效选区或选区过大（最大" + ChainMiningReforged.MAX_SELECTION_SIZE + "方块）"));
            return 0;
        }

        BlockState state = blockState.getBlockState();
        Block block = state.getBlock();

        source.sendFeedback((Supplier<Text>) Text.literal("开始替换方块..."), false);

        final int[] changed = {0};
        area.forEachBlock(source.getWorld(), pos -> {
            if (source.getWorld().setBlockState(pos, state)) {
                changed[0]++;
            }
        });

        source.sendFeedback((Supplier<Text>) Text.literal("成功替换 " + changed[0] + " 个方块为 " + block.getName()), false);
        return changed[0];
    }
}
