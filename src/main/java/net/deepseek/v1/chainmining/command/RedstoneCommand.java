package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deepseek.v1.chainmining.core.confug.ConfigManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

// RedstoneCommand.java
public class RedstoneCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("redstone")
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("set")
                        .then(argument("value", IntegerArgumentType.integer(1))
                                .executes(ctx -> setValue(ctx, IntegerArgumentType.getInteger(ctx, "value"))))
                )
                .then(literal("add")
                        .then(argument("value", IntegerArgumentType.integer())
                                .executes(ctx -> addValue(ctx, IntegerArgumentType.getInteger(ctx, "value"))))
                )
        );
    }

    private static int setValue(CommandContext<ServerCommandSource> ctx, int value) {
        int max = ConfigManager.getMaxPowerDistance();
        int newValue = Math.min(value, max);
        ConfigManager.setPowerDistance(newValue);
        ctx.getSource().sendFeedback(() ->
                Text.literal("Set redstone power distance to: " + newValue), true);
        return 1;
    }

    private static int addValue(CommandContext<ServerCommandSource> ctx, int delta) {
        int current = ConfigManager.getCurrentPowerDistance();
        int newValue = Math.min(current + delta, ConfigManager.getMaxPowerDistance());
        ConfigManager.setPowerDistance(newValue);
        ctx.getSource().sendFeedback(() ->
                Text.literal("Adjusted redstone power distance to: " + newValue), true);
        return 1;
    }
}