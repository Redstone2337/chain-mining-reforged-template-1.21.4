// HomePayloads.java
package net.deepseek.v1.chainmining.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class HomePayloads {
    public static final Identifier SET_HOME = Identifier.of("chainmining", "set_home");
    public static final Identifier TELEPORT_TO_HOME = Identifier.of("chainmining", "teleport_home");
    public static final Identifier HOME_DATA_SYNC = Identifier.of("chainmining", "home_data_sync");

    // 设置家的网络包
    public record SetHomePayload(String homeName, double x, double y, double z, String dimension) implements CustomPayload {
        public static final CustomPayload.Id<SetHomePayload> ID = new CustomPayload.Id<>(SET_HOME);

        public static final PacketCodec<PacketByteBuf, SetHomePayload> CODEC = PacketCodec.of(
                (value, buf) -> {
                    buf.writeString(value.homeName);
                    buf.writeDouble(value.x);
                    buf.writeDouble(value.y);
                    buf.writeDouble(value.z);
                    buf.writeString(value.dimension);
                },
                buf -> new SetHomePayload(
                        buf.readString(),
                        buf.readDouble(),
                        buf.readDouble(),
                        buf.readDouble(),
                        buf.readString()
                )
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    // 传送回家的网络包
    public record TeleportHomePayload(String homeName) implements CustomPayload {
        public static final CustomPayload.Id<TeleportHomePayload> ID = new CustomPayload.Id<>(TELEPORT_TO_HOME);

        public static final PacketCodec<PacketByteBuf, TeleportHomePayload> CODEC = PacketCodec.of(
                (value, buf) -> buf.writeString(value.homeName),
                buf -> new TeleportHomePayload(buf.readString())
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    // 同步 home 数据的网络包
    public record HomeDataSyncPayload(NbtCompound homeData) implements CustomPayload {
        public static final CustomPayload.Id<HomeDataSyncPayload> ID = new CustomPayload.Id<>(HOME_DATA_SYNC);

        public static final PacketCodec<PacketByteBuf, HomeDataSyncPayload> CODEC = PacketCodec.of(
                (value, buf) -> buf.writeNbt(value.homeData),
                buf -> new HomeDataSyncPayload(buf.readNbt())
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}