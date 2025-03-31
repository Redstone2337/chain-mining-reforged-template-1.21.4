package net.deepseek.v1.chainmining.util;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectionArea {
    private final BlockPos minPos;
    private final BlockPos maxPos;
    private final int volume;

    public SelectionArea(BlockPos pos1, BlockPos pos2) {
        this.minPos = new BlockPos(
                Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ())
        );
        this.maxPos = new BlockPos(
                Math.max(pos1.getX(), pos2.getX()),
                Math.max(pos1.getY(), pos2.getY()),
                Math.max(pos1.getZ(), pos2.getZ())
        );
        this.volume = (maxPos.getX() - minPos.getX() + 1)
                * (maxPos.getY() - minPos.getY() + 1)
                * (maxPos.getZ() - minPos.getZ() + 1);
    }

    public boolean isValid() {
        return minPos != null && maxPos != null
                && volume <= ChainMiningReforged.MAX_SELECTION_SIZE;
    }

    public int getVolume() {
        return volume;
    }

    public List<BlockPos> getEdgeBlocks() {
        List<BlockPos> edges = new ArrayList<>();

        // 添加X轴方向的边
        for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
            edges.add(new BlockPos(x, minPos.getY(), minPos.getZ()));
            edges.add(new BlockPos(x, minPos.getY(), maxPos.getZ()));
            edges.add(new BlockPos(x, maxPos.getY(), minPos.getZ()));
            edges.add(new BlockPos(x, maxPos.getY(), maxPos.getZ()));
        }

        // 添加Y轴方向的边（避免重复）
        for (int y = minPos.getY() + 1; y < maxPos.getY(); y++) {
            edges.add(new BlockPos(minPos.getX(), y, minPos.getZ()));
            edges.add(new BlockPos(minPos.getX(), y, maxPos.getZ()));
            edges.add(new BlockPos(maxPos.getX(), y, minPos.getZ()));
            edges.add(new BlockPos(maxPos.getX(), y, maxPos.getZ()));
        }

        // 添加Z轴方向的边（内部）
        for (int z = minPos.getZ() + 1; z < maxPos.getZ(); z++) {
            edges.add(new BlockPos(minPos.getX(), minPos.getY(), z));
            edges.add(new BlockPos(minPos.getX(), maxPos.getY(), z));
            edges.add(new BlockPos(maxPos.getX(), minPos.getY(), z));
            edges.add(new BlockPos(maxPos.getX(), maxPos.getY(), z));
        }

        return edges;
    }

    public void forEachBlock(World world, Consumer<BlockPos> action) {
        if (volume > 10_000) {
            // 异步处理大型选区
            world.getServer().submit(() -> {
                for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                    for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
                        for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                            action.accept(new BlockPos(x, y, z));
                        }
                    }
                }
            });
        } else {
            // 同步处理小型选区
            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
                    for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                        action.accept(new BlockPos(x, y, z));
                    }
                }
            }
        }
    }
}
