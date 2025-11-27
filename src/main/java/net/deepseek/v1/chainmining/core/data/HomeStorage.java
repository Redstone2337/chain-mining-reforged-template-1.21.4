// HomeStorage.java
package net.deepseek.v1.chainmining.core.data;

import com.mojang.serialization.Codec;
import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.InternalApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HomeStorage {
    // 使用 Map<String, HomeData> 来存储多个 home
    private static final AttachmentType<Map<String, HomeData>> HOME_DATA_ATTACHMENT =
            AttachmentRegistry.createPersistent(
                    Identifier.of(ChainMiningReforged.MOD_ID, "home_data"),
                    createHomeMapCodec()
            );

    /**
     * 创建 Map<String, HomeData> 的 Codec
     */
    private static Codec<Map<String, HomeData>> createHomeMapCodec() {
        return Codec.unboundedMap(Codec.STRING, HomeData.CODEC);
    }

    /**
     * 设置 home 位置
     */
    public static void setHome(ServerPlayerEntity player, String homeName, Vec3d position, RegistryKey<World> dimension) {
        Map<String, HomeData> homeMap = player.getAttachedOrCreate(HOME_DATA_ATTACHMENT, HashMap::new);

        HomeData homeData = new HomeData(position, dimension, System.currentTimeMillis());
        homeMap.put(homeName, homeData);

        player.setAttached(HOME_DATA_ATTACHMENT, homeMap);
    }

    /**
     * 获取指定的 home 数据
     */
    public static Optional<HomeData> getHome(ServerPlayerEntity player, String homeName) {
        Map<String, HomeData> homeMap = player.getAttached(HOME_DATA_ATTACHMENT);
        if (homeMap != null && homeMap.containsKey(homeName)) {
            return Optional.of(homeMap.get(homeName));
        }
        return Optional.empty();
    }

    /**
     * 获取所有 home 数据
     */
    public static Map<String, HomeData> getAllHomes(ServerPlayerEntity player) {
        Map<String, HomeData> homeMap = player.getAttached(HOME_DATA_ATTACHMENT);
        return homeMap != null ? new HashMap<>(homeMap) : new HashMap<>();
    }

    /**
     * 删除指定的 home
     */
    public static boolean deleteHome(ServerPlayerEntity player, String homeName) {
        Map<String, HomeData> homeMap = player.getAttached(HOME_DATA_ATTACHMENT);
        if (homeMap != null && homeMap.containsKey(homeName)) {
            homeMap.remove(homeName);
            player.setAttached(HOME_DATA_ATTACHMENT, homeMap);
            return true;
        }
        return false;
    }

    /**
     * 检查是否存在指定的 home
     */
    public static boolean hasHome(ServerPlayerEntity player, String homeName) {
        Map<String, HomeData> homeMap = player.getAttached(HOME_DATA_ATTACHMENT);
        return homeMap != null && homeMap.containsKey(homeName);
    }

    /**
     * 获取 home 数量
     */
    public static int getHomeCount(ServerPlayerEntity player) {
        Map<String, HomeData> homeMap = player.getAttached(HOME_DATA_ATTACHMENT);
        return homeMap != null ? homeMap.size() : 0;
    }
}