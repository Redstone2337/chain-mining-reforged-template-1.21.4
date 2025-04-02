package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.deepseek.v1.chainmining.core.config.ConfigManager;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.List;

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
                .then(literal("remove")
                        .then(argument("target", StringArgumentType.word())
                                .suggests((ctx, builder) ->
                                        CommandSource.suggestMatching(List.of("current", "max"), builder))
                                .then(argument("value", IntegerArgumentType.integer(1))
                                        .executes(ctx -> removeValue(
                                                ctx,
                                                StringArgumentType.getString(ctx, "target"),
                                                IntegerArgumentType.getInteger(ctx, "value")
                                        ))
                                )
                        )
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

    private static int removeValue(CommandContext<ServerCommandSource> ctx, String target, int value) throws CommandSyntaxException {
        switch (target.toLowerCase()) {
            case "current" -> {
                int newCurrent = Math.max(1, ConfigManager.getCurrentPowerDistance() - value);
                ConfigManager.setPowerDistance(newCurrent);
                ctx.getSource().sendFeedback(() ->
                        Text.literal("Reduced current power distance to: " + newCurrent), true);
            }
            case "max" -> {
                int newMax = Math.max(1, ConfigManager.getMaxPowerDistance() - value);
                ConfigManager.setMaxPowerDistance(newMax);
                ctx.getSource().sendFeedback(() ->
                        Text.literal("Reduced max power distance to: " + newMax), true);
            }
            default -> throw new SimpleCommandExceptionType(Text.literal("Invalid target (use 'current' or 'max')")).create();
        }
        return 1;
    }
}