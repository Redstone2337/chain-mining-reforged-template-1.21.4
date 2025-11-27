// HomeNetworkHandler.java
package net.deepseek.v1.chainmining.network;

import net.deepseek.v1.chainmining.core.data.HomeData;
import net.deepseek.v1.chainmining.core.data.HomeStorage;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

public class HomeNetworkHandler {

    public static void initialize() {
        // 注册网络包类型
        PayloadTypeRegistry.playC2S().register(HomePayloads.SetHomePayload.ID, HomePayloads.SetHomePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(HomePayloads.TeleportHomePayload.ID, HomePayloads.TeleportHomePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HomePayloads.HomeDataSyncPayload.ID, HomePayloads.HomeDataSyncPayload.CODEC);

        // 处理客户端设置家的请求
        ServerPlayNetworking.registerGlobalReceiver(HomePayloads.SetHomePayload.ID,
                (payload, context) -> {
                    ServerPlayerEntity player = context.player();
                    String homeName = payload.homeName();

                    // 验证权限和参数
                    if (homeName.length() > 16) {
                        player.sendMessage(Text.literal("Home name too long (max 16 characters)"), false);
                        return;
                    }

                    if (HomeStorage.getHomeCount(player) >= 5) { // 限制最多5个home
                        player.sendMessage(Text.literal("You can only have up to 5 homes"), false);
                        return;
                    }

                    // 创建位置和维度
                    Vec3d position = new Vec3d(payload.x(), payload.y(), payload.z());
                    RegistryKey<World> dimension = RegistryKey.of(RegistryKeys.WORLD,
                            Identifier.tryParse(payload.dimension()));

                    // 保存到持久化存储
                    HomeStorage.setHome(player, homeName, position, dimension);

                    player.sendMessage(Text.literal("Home '" + homeName + "' set successfully!"), false);
                });

        // 处理客户端传送回家的请求
        ServerPlayNetworking.registerGlobalReceiver(HomePayloads.TeleportHomePayload.ID,
                (payload, context) -> {
                    ServerPlayerEntity player = context.player();
                    String homeName = payload.homeName();

                    HomeStorage.getHome(player, homeName).ifPresentOrElse(
                            homeData -> {
                                teleportToHome(player, homeData, homeName);
                            },
                            () -> player.sendMessage(Text.literal("You haven't set a home named '" + homeName + "'"), false)
                    );
                });
    }

    /**
     * 传送玩家到指定的家
     */
    private static void teleportToHome(ServerPlayerEntity player, HomeData homeData, String homeName) {
        try {
            ServerWorld targetWorld = Objects.requireNonNull(player.getServer()).getWorld(homeData.dimension());

            if (targetWorld == null) {
                player.sendMessage(Text.literal("Cannot teleport to home '" + homeName + "': target dimension not found"), false);
                return;
            }

            Vec3d homePos = homeData.position();

            // 检查目标位置是否安全
            if (!isSafeLocation(targetWorld, homePos, player)) {
                // 如果目标位置不安全，尝试寻找附近的安全位置
                Vec3d safePos = findSafeLocation(targetWorld, homePos, player);
                if (safePos != null) {
                    homePos = safePos;
                    player.sendMessage(Text.literal("Adjusted home position to nearby safe location"), false);
                } else {
                    player.sendMessage(Text.literal("Cannot teleport to home '" + homeName + "': unsafe location"), false);
                    return;
                }
            }

            // 方法1: 使用完整的 teleport 方法（支持跨维度）
            if (player.getServerWorld() != targetWorld) {
                // 跨维度传送
                player.teleport(
                        targetWorld,
                        homePos.x,
                        homePos.y,
                        homePos.z,
                        PositionFlag.ROT, // 只保持旋转，不保持速度等
                        player.getYaw(),
                        player.getPitch(),
                        true // resetCamera
                );
            } else {
                // 同维度传送，可以使用更简单的方法
                player.teleport(
                        homePos.x,
                        homePos.y,
                        homePos.z,
                        true // particleEffects
                );
            }

            player.sendMessage(Text.literal("Teleported to home '" + homeName + "'"), false);

        } catch (Exception e) {
            player.sendMessage(Text.literal("Failed to teleport to home '" + homeName + "': " + e.getMessage()), false);
        }
    }

    /**
     * 检查位置是否安全（有站立的地方且不会卡住）
     */
    private static boolean isSafeLocation(ServerWorld world, Vec3d pos, Entity entity) {
        // 创建玩家碰撞箱
        Box playerBox = createPlayerBoundingBox(pos);

        // 检查玩家碰撞箱内是否有固体方块
        // 使用 isSpaceEmpty(Box box) 检查碰撞箱是否为空
        if (!world.isSpaceEmpty(playerBox)) {
            return false;
        }

        // 检查玩家站立位置下方是否有固体方块
        BlockPos belowPos = BlockPos.ofFloored(pos.x, pos.y - 0.1, pos.z);
        if (!world.getBlockState(belowPos).isSolid()) {
            return false;
        }

        // 使用 isSpaceEmpty(Entity entity, Box box) 进行更精确的检查
        // 这个方法会考虑实体的特殊碰撞规则
        if (!world.isSpaceEmpty(entity, playerBox)) {
            return false;
        }

        // 检查流体（可选，根据需求决定）
        // 使用 isSpaceEmpty(Entity entity, Box box, boolean checkFluid)
        // 如果不想检查流体，可以设为false
        return world.isSpaceEmpty(entity, playerBox, true);
    }

    /**
     * 创建玩家的边界框
     */
    private static Box createPlayerBoundingBox(Vec3d pos) {
        // 玩家通常宽0.6米，高1.8米
        double halfWidth = 0.3;
        double height = 1.8;

        return new Box(
                pos.x - halfWidth, pos.y, pos.z - halfWidth,
                pos.x + halfWidth, pos.y + height, pos.z + halfWidth
        );
    }

    /**
     * 在目标位置附近寻找安全位置
     */
    private static Vec3d findSafeLocation(ServerWorld world, Vec3d originalPos, Entity entity) {
        // 简单的算法：在原始位置周围寻找安全位置
        // 先尝试同一高度
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                Vec3d testPos = originalPos.add(x, 0, z);
                if (isSafeLocation(world, testPos, entity)) {
                    return testPos;
                }
            }
        }

        // 如果同一高度没有安全位置，尝试上下寻找
        for (int y = -3; y <= 3; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Vec3d testPos = originalPos.add(x, y, z);
                    if (isSafeLocation(world, testPos, entity)) {
                        return testPos;
                    }
                }
            }
        }

        return null;
    }

    /**
     * 传送玩家到默认的家
     */
    public static void teleportToDefaultHome(ServerPlayerEntity player) {
        HomeStorage.getHome(player, "home").ifPresentOrElse(
                homeData -> teleportToHome(player, homeData, "home"),
                () -> player.sendMessage(Text.literal("You haven't set a default home"), false)
        );
    }

    /**
     * 传送玩家到指定的家（服务器端调用）
     */
    public static void teleportToHome(ServerPlayerEntity player, String homeName) {
        HomeStorage.getHome(player, homeName).ifPresentOrElse(
                homeData -> teleportToHome(player, homeData, homeName),
                () -> player.sendMessage(Text.literal("You haven't set a home named '" + homeName + "'"), false)
        );
    }

    /**
     * 检查玩家是否可以设置家（位置安全检查）
     */
    public static boolean canSetHomeAtLocation(ServerPlayerEntity player, Vec3d pos) {
        return isSafeLocation(player.getServerWorld(), pos, player);
    }
}