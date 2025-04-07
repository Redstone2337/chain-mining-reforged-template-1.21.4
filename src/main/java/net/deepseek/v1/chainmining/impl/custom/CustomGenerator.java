package net.deepseek.v1.chainmining.impl.custom;

import net.deepseek.v1.chainmining.api.VeinGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;


import java.util.Random;
import java.util.function.Predicate;

/**
 * 自定义矿脉形状生成器，允许用户通过修改器自定义矿脉的生成逻辑。
 * 默认实现为简单的球形矿脉，但可以通过添加自定义修改器来调整生成行为。
 *
 * @version 0.0.1
 * @see VeinGenerator.VeinShapeGenerator
 */
public class CustomGenerator implements VeinGenerator.VeinShapeGenerator {
    private final Random random = new Random();
    private VeinGenerator.Modifier modifier = (w, o, r, d, t, s, c) -> c; // 默认无修改

    /**
     * 生成矿脉。
     * 默认实现为简单的球形矿脉，但可以通过添加自定义修改器来调整生成行为。
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
        // 默认实现: 简单的球形但可以轻松修改
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

        // 应用自定义修改器
        return modifier.modify(world, origin, radius, density, target, state, count);
    }

    /**
     * 添加自定义修改器。
     * 修改器可以在生成完成后对方块数量或其他逻辑进行调整。
     *
     * @param modifier 自定义修改器。
     * @return 当前生成器实例，支持链式调用。
     */
    public CustomGenerator withModifier(VeinGenerator.Modifier modifier) {
        this.modifier = modifier;
        return this;
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
