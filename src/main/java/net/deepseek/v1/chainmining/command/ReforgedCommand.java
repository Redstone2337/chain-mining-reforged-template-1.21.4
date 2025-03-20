package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ReforgedCommand {
    public static LiteralCommandNode<ServerCommandSource> ChainMiningNode;

    public static LiteralArgumentBuilder<ServerCommandSource> register(CommandRegistryAccess commandRegistryAccess) {

        ChainMiningNode = (LiteralCommandNode<ServerCommandSource>) ChainMiningCommands.register(commandRegistryAccess).getRedirect().getChild("chainmining");

        return CommandManager.literal("cmr")
                .requires(src -> src.hasPermissionLevel(2))
                .redirect(ChainMiningNode)
                .executes(context -> ChainMiningNode.getCommand().run(context));
    }
}
