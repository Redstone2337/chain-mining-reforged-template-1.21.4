package net.deepseek.v1.chainmining.core.data;

import net.deepseek.v1.chainmining.util.SelectionArea;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSelectionData {
    private static final Map<UUID, PlayerSelectionData> PLAYER_DATA = new HashMap<>();

    private BlockPos firstPos;
    private BlockPos secondPos;

    public static PlayerSelectionData get(PlayerEntity player) {
        return PLAYER_DATA.computeIfAbsent(player.getUuid(), uuid -> new PlayerSelectionData());
    }

    public static void clearAll() {
        PLAYER_DATA.clear();
    }

    public void setFirstPos(BlockPos pos) {
        this.firstPos = pos;
    }

    public void setSecondPos(BlockPos pos) {
        this.secondPos = pos;
    }

    public SelectionArea getSelection() {
        if (firstPos == null || secondPos == null) {
            return null;
        }
        return new SelectionArea(firstPos, secondPos);
    }

    // 保存到玩家NBT
    public void writeToNbt(NbtCompound nbt) {
        if (firstPos != null) {
            nbt.putLong("FirstPos", firstPos.asLong());
        }
        if (secondPos != null) {
            nbt.putLong("SecondPos", secondPos.asLong());
        }
    }

    // 从NBT读取
    public void readFromNbt(NbtCompound nbt) {
        if (nbt.contains("FirstPos")) {
            firstPos = BlockPos.fromLong(nbt.getLong("FirstPos"));
        }
        if (nbt.contains("SecondPos")) {
            secondPos = BlockPos.fromLong(nbt.getLong("SecondPos"));
        }
    }
}
