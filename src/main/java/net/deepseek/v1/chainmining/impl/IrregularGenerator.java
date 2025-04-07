package net.deepseek.v1.chainmining.impl;

import net.deepseek.v1.chainmining.api.VeinGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;


import java.util.Random;
import java.util.function.Predicate;

/**
 * 不规则形状的矿脉生成器，用于生成具有不规则形状的矿脉。
 * 该生成器基于球形矿脉，通过随机偏移和剔除部分方块来增加不规则性。
 * 生成的矿脉形状更加自然和复杂，适合用于模拟真实世界的矿脉分布。
 *
 * @version 0.0.1
 * @see VeinGenerator.VeinShapeGenerator
 */
public class IrregularGenerator implements VeinGenerator.VeinShapeGenerator {
    private final Random random = new Random();

    /**
     * 生成不规则形状的矿脉。
     * 该方法首先基于球形矿脉进行生成，然后通过随机偏移和剔除部分方块来增加不规则性。
     *
     * @param world   要生成矿脉的世界。
     * @param origin  矿脉的中心位置。
     * @param radius  矿脉的半径。
     * @param density 矿脉的密度（范围为0.0到1.0）。
     * @param target  用于测试方块是否可以被替换的条件。
     * @param state   要生成的方块状态。
     * @return 生成的方块数量。
     */
    @Override
    public int generate(WorldAccess world, BlockPos origin, int radius, float density,
                        Predicate<BlockState> target, BlockState state) {
        int count = 0;
        int radiusSq = radius * radius;

        // 不规则形状通过多次随机偏移实现
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    // 基础球形检查
                    if (x * x + y * y + z * z <= radiusSq) {
                        // 添加不规则性 - 随机剔除一些方块
                        if (random.nextFloat() > 0.3f) {
                            BlockPos pos = origin.add(x, y, z);
                            // 随机偏移位置增加不规则性
                            if (random.nextFloat() < 0.2f) {
                                pos = pos.add(
                                        random.nextInt(3) - 1,
                                        random.nextInt(3) - 1,
                                        random.nextInt(3) - 1
                                );
                            }

                            if (shouldGenerate(world, pos, target, density)) {
                                world.setBlockState(pos, state, 2);
                                count++;
                            }
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
