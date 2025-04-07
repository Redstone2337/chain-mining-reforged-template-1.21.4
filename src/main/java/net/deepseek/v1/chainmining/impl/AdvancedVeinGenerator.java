package net.deepseek.v1.chainmining.impl;

import net.deepseek.v1.chainmining.api.VeinGenerator;
import net.deepseek.v1.chainmining.impl.custom.CustomGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.function.Predicate;

/**
 * 高级矿脉生成器，支持多种形状和自定义生成逻辑。
 * 用户可以通过设置不同的形状（如球形、立方体、自定义形状）和密度来生成矿脉。
 * 当使用自定义形状时，可以添加自定义修改器来进一步调整生成行为。
 *
 * @version 0.0.1
 * @see VeinGenerator
 */
public class AdvancedVeinGenerator implements VeinGenerator {
    private VeinShape shape = VeinShape.SPHERICAL;
    private VeinGenerator.VeinShapeGenerator shapeGenerator;
    private float density = 0.7f;
    private int maxRadius = 8;

    /**
     * 构造一个高级矿脉生成器，默认形状为球形。
     */
    public AdvancedVeinGenerator() {
        this.shapeGenerator = shape.createGenerator();
    }

    /**
     * 为自定义生成器添加修改器。
     * 仅当形状为CUSTOM时有效。
     *
     * @param modifier 自定义修改器。
     */
    public void addCustomModifier(VeinGenerator.Modifier modifier) {
        if (shape != VeinShape.CUSTOM) {
            throw new IllegalStateException("Can only add modifiers for CUSTOM shape");
        }
        if (shapeGenerator instanceof CustomGenerator) {
            ((CustomGenerator) shapeGenerator).withModifier(modifier);
        }
    }

    /**
     * 设置自定义生成器并自动将形状设为CUSTOM。
     *
     * @param generator 自定义生成器。
     */
    public void setCustomGenerator(VeinGenerator.VeinShapeGenerator generator) {
        this.shape = VeinShape.CUSTOM;
        this.shapeGenerator = generator;
    }

    /**
     * 生成矿脉。
     *
     * @param world   要生成矿脉的世界。
     * @param origin  矿脉的中心位置。
     * @param config  生成配置（目前未使用）。
     * @param target  用于测试方块是否可以被替换的条件。
     * @param state   要生成的方块状态。
     * @return 生成的方块数量。
     */
    @Override
    public int generateVein(WorldAccess world, BlockPos origin, FeatureConfig config,
                            Predicate<BlockState> target, BlockState state) {
        if (shapeGenerator == null) {
            throw new IllegalStateException("Shape generator not set for custom shape");
        }
        return shapeGenerator.generate(world, origin, maxRadius, density, target, state);
    }

    /**
     * 获取当前矿脉形状。
     *
     * @return 当前矿脉形状。
     */
    @Override
    public VeinShape getShape() {
        return shape;
    }

    /**
     * 设置矿脉形状。
     *
     * @param shape 矿脉形状。
     */
    @Override
    public void setShape(VeinShape shape) {
        this.shape = shape;
        this.shapeGenerator = shape.createGenerator();
    }

    /**
     * 获取矿脉形状生成器。
     *
     * @return 矿脉形状生成器。
     */
    @Override
    public VeinShapeGenerator getShapeGenerator() {
        return shapeGenerator;
    }

    /**
     * 设置自定义矿脉形状生成器。
     * 仅当形状为CUSTOM时有效。
     *
     * @param generator 自定义矿脉形状生成器。
     */
    public void setCustomShapeGenerator(VeinShapeGenerator generator) {
        if (shape != VeinGenerator.VeinShape.CUSTOM) {
            throw new IllegalStateException("Can only set custom generator for CUSTOM shape");
        }
        this.shapeGenerator = generator;
    }

    /**
     * 获取矿脉密度。
     *
     * @return 矿脉密度（范围为0.0到1.0）。
     */
    @Override
    public float getDensity() {
        return density;
    }

    /**
     * 设置矿脉密度。
     *
     * @param density 矿脉密度（范围为0.0到1.0）。
     */
    @Override
    public void setDensity(float density) {
        this.density = Math.max(0, Math.min(1, density));
    }

    /**
     * 获取矿脉最大半径。
     *
     * @return 矿脉最大半径。
     */
    @Override
    public int getMaxRadius() {
        return maxRadius;
    }

    /**
     * 设置矿脉最大半径。
     *
     * @param radius 矿脉最大半径。
     */
    @Override
    public void setMaxRadius(int radius) {
        this.maxRadius = Math.max(1, radius);
    }
}
