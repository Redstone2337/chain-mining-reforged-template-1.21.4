package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetHomeCommand {
    // 存储玩家home位置的Map
    private static final Map<UUID, Vec3d> homePositions = new HashMap<>();

    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("sethome")
                .then(CommandManager.argument("HomePos", Vec3ArgumentType.vec3())
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                            Vec3d pos = Vec3ArgumentType.getVec3(context, "HomePos");

                            // 更新或设置新的 home 位置
                            homePositions.put(player.getUuid(), pos);
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

                    // 更新或设置新的 home 位置
                    homePositions.put(player.getUuid(), pos);
                    context.getSource().sendFeedback(
                            () -> Text.literal("Home set to current position " + formatVec3d(pos)),
                            false
                    );
                    return 1;
                });
    }

    // 提供给其他类访问 home 位置的方法
    public static Vec3d getHomePosition(UUID playerUuid) {
            return homePositions.get(playerUuid);
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

