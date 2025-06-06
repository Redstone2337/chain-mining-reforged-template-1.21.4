package net.deepseek.v1.chainmining.impl;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.deepseek.v1.chainmining.api.VeinGenerator;

import java.util.Random;
import java.util.function.Predicate;

/**
 * 球体形状的矿脉生成器，用于生成球体形状的矿脉。
 * 该生成器以指定的中心位置为原点，生成一个半径为指定值的球体。
 * 密度参数用于控制每个方块生成的概率。
 *
 * @version 0.0.1
 * @see VeinGenerator.VeinShapeGenerator
 */
public class SphericalGenerator implements VeinGenerator.VeinShapeGenerator {
    private final Random random = new Random();

    /**
     * 在指定的世界中生成一个球体形状的矿脉。
     *
     * @param world   要生成矿脉的世界。
     * @param origin  球体矿脉的中心位置。
     * @param radius  球体的半径。
     * @param density 每个方块生成的概率（范围为0.0到1.0）。
     * @param target  用于测试方块是否可以被替换的条件。
     * @param state   要生成的方块状态。
     * @return 生成的方块数量。
     */
    @Override
    public int generate(WorldAccess world, BlockPos origin, int radius, float density,
                        Predicate<BlockState> target, BlockState state) {
        int count = 0;
        int radiusSq = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= radiusSq) {
                        BlockPos pos = origin.add(x, y, z);
                        if (shouldGenerate(world, pos, target, density)) {
                            world.setBlockState(pos, state, 2);
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    /**
     * 检查是否应在指定位置生成方块。
     *
     * @param world   方块生成的世界。
     * @param pos     方块的位置。
     * @param target  用于测试方块是否可以被替换的条件。
     * @param density 方块生成的概率（范围为0.0到1.0）。
     * @return 如果应该生成方块，则返回true，否则返回false。
     */
    private boolean shouldGenerate(WorldAccess world, BlockPos pos,
                                   Predicate<BlockState> target, float density) {
        return target.test(world.getBlockState(pos)) && random.nextFloat() < density;
    }
}
