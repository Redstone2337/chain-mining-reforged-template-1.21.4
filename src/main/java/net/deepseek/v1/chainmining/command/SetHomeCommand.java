// SetHomeCommand.java
package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.deepseek.v1.chainmining.core.data.HomeStorage;
import net.deepseek.v1.chainmining.network.HomeNetworkHandler;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class SetHomeCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("sethome")
                .then(CommandManager.argument("HomePos", Vec3ArgumentType.vec3())
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                            Vec3d pos = Vec3ArgumentType.getVec3(context, "HomePos");

                            // 使用新的安全检查
                            if (!HomeNetworkHandler.canSetHomeAtLocation(player, pos)) {
                                context.getSource().sendError(Text.literal("This location is not safe for a home"));
                                return 0;
                            }

                            // 使用新的存储系统
                            HomeStorage.setHome(player, "home", pos, player.getServerWorld().getRegistryKey());

                            context.getSource().sendFeedback(
                                    () -> Text.literal("Home set to " + formatVec3d(pos)),
                                    false
                            );
                            return 1;
                        })
                )
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    Vec3d pos = player.getPos();

                    // 使用新的安全检查
                    if (!HomeNetworkHandler.canSetHomeAtLocation(player, pos)) {
                        context.getSource().sendError(Text.literal("This location is not safe for a home"));
                        return 0;
                    }

                    // 使用新的存储系统
                    HomeStorage.setHome(player, "home", pos, player.getServerWorld().getRegistryKey());

                    context.getSource().sendFeedback(
                            () -> Text.literal("Home set to current position " + formatVec3d(pos)),
                            false
                    );
                    return 1;
                });
    }

    private static String formatVec3d(Vec3d vec) {
        return String.format(
                "(%.1f, %.1f, %.1f)",
                vec.getX(),
                vec.getY(),
                vec.getZ()
        );
    }
}