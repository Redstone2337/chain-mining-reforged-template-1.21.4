package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.deepseek.v1.chainmining.core.data.HomeStorage;
import net.deepseek.v1.chainmining.network.HomeNetworkHandler;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

public class BackCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("back")
                .then(CommandManager.argument("HomePos", Vec3ArgumentType.vec3())
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                            Vec3d pos = Vec3ArgumentType.getVec3(context, "HomePos");

                            // 使用新的传送系统
                            if (teleportPlayer(player, pos, player.getServerWorld().getRegistryKey())) {
                                context.getSource().sendFeedback(
                                        () -> Text.literal("Teleported to " + formatVec3d(pos)),
                                        false
                                );
                                return 1;
                            } else {
                                context.getSource().sendError(Text.literal("Failed to teleport to the specified location"));
                                return 0;
                            }
                        })
                )
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

                    // 使用新的存储系统获取默认家
                    HomeStorage.getHome(player, "home").ifPresentOrElse(
                            homeData -> {
                                // 使用新的传送系统
                                HomeNetworkHandler.teleportToHome(player, "home");
                                context.getSource().sendFeedback(
                                        () -> Text.literal("Teleported to home " + formatVec3d(homeData.position())),
                                        false
                                );
                            },
                            () -> context.getSource().sendError(Text.literal("No home position set!"))
                    );

                    return 1;
                });
    }

    /**
     * 传送玩家到指定位置（同维度）
     */
    private static boolean teleportPlayer(ServerPlayerEntity player, Vec3d pos, net.minecraft.registry.RegistryKey<net.minecraft.world.World> dimension) {
        try {
            // 使用新的安全检查
            if (!HomeNetworkHandler.canSetHomeAtLocation(player, pos)) {
                player.sendMessage(Text.literal("Warning: The target location might not be safe"), false);
                // 但仍然允许传送，只是给出警告
            }

            // 使用新的传送系统 - 同维度传送
            player.teleport(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    true // particleEffects
            );

            return true;
        } catch (Exception e) {
            player.sendMessage(Text.literal("Teleportation failed: " + e.getMessage()), false);
            return false;
        }
    }

    /**
     * 传送玩家到指定位置（跨维度）
     */
    private static boolean teleportPlayer(ServerPlayerEntity player, Vec3d pos, net.minecraft.registry.RegistryKey<net.minecraft.world.World> dimension, boolean crossDimension) {
        if (!crossDimension) {
            return teleportPlayer(player, pos, dimension);
        }

        try {
            net.minecraft.server.world.ServerWorld targetWorld = player.getServer().getWorld(dimension);

            if (targetWorld == null) {
                player.sendMessage(Text.literal("Target dimension not found"), false);
                return false;
            }

            // 使用新的安全检查
            if (!HomeNetworkHandler.canSetHomeAtLocation(player, pos)) {
                player.sendMessage(Text.literal("Warning: The target location might not be safe").formatted(Formatting.YELLOW), false);
                // 但仍然允许传送，只是给出警告
            }

            // 使用新的传送系统 - 跨维度传送
            player.teleport(
                    targetWorld,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    PositionFlag.ROT,
                    player.getYaw(),
                    player.getPitch(),
                    true // resetCamera
            );

            return true;
        } catch (Exception e) {
            player.sendMessage(Text.literal("Teleportation failed: " + e.getMessage()), false);
            return false;
        }
    }

    private static String formatVec3d(Vec3d vec) {
        return String.format(
                "(%.1f, %.1f, %.1f)",
                vec.getX(),
                vec.getY(),
                vec.getZ()
        );
    }

    /**
     * 为其他命令提供传送功能
     */
    public static boolean teleportToPosition(ServerPlayerEntity player, Vec3d position) {
        return teleportPlayer(player, position, player.getServerWorld().getRegistryKey());
    }

    /**
     * 为其他命令提供跨维度传送功能
     */
    public static boolean teleportToPosition(ServerPlayerEntity player, Vec3d position, net.minecraft.registry.RegistryKey<net.minecraft.world.World> dimension) {
        boolean crossDimension = !player.getServerWorld().getRegistryKey().equals(dimension);
        return teleportPlayer(player, position, dimension, crossDimension);
    }
}