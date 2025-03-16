package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ReforgedCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register(CommandRegistryAccess commandRegistryAccess) {
        return CommandManager.literal("cmr")
                .requires(src -> src.hasPermissionLevel(2))
                .redirect(ChainMiningCommands.register(commandRegistryAccess).getRedirect().getChild("chainmining"));
    }
}
