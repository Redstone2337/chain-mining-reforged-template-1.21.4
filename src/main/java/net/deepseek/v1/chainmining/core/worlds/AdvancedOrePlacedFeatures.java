package net.deepseek.v1.chainmining.core.worlds;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.block.BlockState;
import net.deepseek.v1.chainmining.api.VeinGenerator;
import net.deepseek.v1.chainmining.api.VeinGenerator.VeinShape;

import java.util.function.Predicate;

public class AdvancedOrePlacedFeatures {

    private final VeinGenerator veinGenerator;

    public AdvancedOrePlacedFeatures(VeinGenerator veinGenerator) {
        this.veinGenerator = veinGenerator;
    }

    /**
     * 根据分布规则生成矿脉。
     *
     * @param world      要生成矿脉的世界。
     * @param origin     矿脉的中心位置。
     * @param config     生成配置。
     * @param target     用于测试方块是否可以被替换的条件。
     * @param state      要生成的方块状态。
     * @param count      矿石数量。
     * @param height     矿石高度范围。
     * @return 生成的方块数量。
     */
    public int generateOre(WorldAccess world, BlockPos origin, FeatureConfig config,
                           Predicate<BlockState> target, BlockState state, int count, int height) {
        // 设置矿脉形状（例如球形）
        veinGenerator.setShape(VeinShape.SPHERICAL);
        // 设置矿脉密度（基于 count 参数）
        veinGenerator.setDensity((float) count / 100.0f);
        // 设置矿脉最大半径（基于 height 参数）
        veinGenerator.setMaxRadius(Math.min(height, 8));

        // 生成矿脉
        return veinGenerator.generateVein(world, origin, config, target, state);
    }
/*
    /**
     * 根据分布规则和自定义生成器生成矿脉。
     *
     * @param world      要生成矿脉的世界。
     * @param origin     矿脉的中心位置。
     * @param config     生成配置。
     * @param target     用于测试方块是否可以被替换的条件。
     * @param state      要生成的方块状态。
     * @param count      矿石数量。
     * @param height     矿石高度范围。
     * @param generator  自定义矿脉生成器。
     * @return 生成的方块数量。
     */
   /* public int generateOreWithCustomGenerator(WorldAccess world, BlockPos origin, FeatureConfig config,
                                              Predicate<BlockState> target, BlockState state, int count, int height,
                                              VeinGenerator.VeinShapeGenerator generator) {
        // 设置自定义生成器
//        veinGenerator.setCustomGenerator(generator);
        // 设置矿脉形状为自定义
        veinGenerator.setShape(VeinShape.CUSTOM);
        // 设置矿脉密度（基于 count 参数）
        veinGenerator.setDensity((float) count / 100.0f);
        // 设置矿脉最大半径（基于 height 参数）
        veinGenerator.setMaxRadius(Math.min(height, 8));

        // 生成矿脉
        return veinGenerator.generateVein(world, origin, config, target, state);
    }*/
}
