package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;

public class BackCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("back")
                .then(CommandManager.argument("HomePos", Vec3ArgumentType.vec3())
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                            Vec3d pos = Vec3ArgumentType.getVec3(context, "HomePos");

                            teleportPlayer(player, pos);
                            context.getSource().sendFeedback(
                                    () -> Text.literal("Teleported to " + formatVec3d(pos)),
                                    false
                            );
                            return 1;
                        })
                )
                .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                            Vec3d home = SetHomeCommand.getHomePosition(player.getUuid());

                            if (home == null) {
                                context.getSource().sendError(Text.literal("No home position set!"));
                                return 0;
                            }

                            teleportPlayer(player, home);
                            context.getSource().sendFeedback(
                                    () -> Text.literal("Teleported to home " + formatVec3d(home)),
                                    false
                            );
                            return 1;
                });
    }


    private static void teleportPlayer(ServerPlayerEntity player, Vec3d pos) {
//        player.teleport(
//                player.getServerWorld(),
//                pos.getX(),
//                pos.getY(),
//                pos.getZ(),
//                player.getYaw(),
//                player.getPitch()
//        );
        player.teleport(
                player.getServerWorld(),
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                new HashSet<>(),
                player.getYaw(),
                player.getPitch(),
                false
        );
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

