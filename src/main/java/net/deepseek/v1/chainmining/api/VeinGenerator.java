package net.deepseek.v1.chainmining.api;

import net.deepseek.v1.chainmining.impl.CubicGenerator;
import net.deepseek.v1.chainmining.impl.DiskGenerator;
import net.deepseek.v1.chainmining.impl.IrregularGenerator;
import net.deepseek.v1.chainmining.impl.SphericalGenerator;
import net.deepseek.v1.chainmining.impl.custom.CustomGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.FeatureConfig;


import java.util.function.Predicate;

/**
 * 增强版矿脉生成API接口，用于在Minecraft世界中生成各种形状的矿脉。
 * <p>
 * 该接口提供了生成球形、立方体、圆盘、不规则形状及自定义图案矿脉的方法，
 * 并支持通过密度、半径和生成后修饰器进行自定义配置。
 * 
 * @version 0.0.1
 * @see VeinShape
 * @see VeinShapeGenerator
 */
public interface VeinGenerator {

    /**
     * 在世界中指定位置生成矿脉。
     *
     * @param world  生成矿脉的世界
     * @param origin 矿脉生成的中心位置
     * @param config 生成配置
     * @param target 判断方块是否可被替换的谓词
     * @param state  要放置的方块状态
     * @return 生成的方块数量
     * @throws IllegalArgumentException 如果任何参数为null或无效
     */
    int generateVein(WorldAccess world, BlockPos origin, FeatureConfig config,
                     Predicate<BlockState> target, BlockState state);

    /**
     * 检查是否应该在指定位置生成矿脉方块。
     * 默认实现会测试当前位置的方块是否符合目标谓词。
     *
     * @param world  要检查的世界
     * @param pos    要检查的位置
     * @param target 用于测试现有方块的谓词
     * @return 如果可以在此位置生成矿脉方块返回true，否则返回false
     */
    default boolean shouldGenerateAt(WorldAccess world, BlockPos pos, Predicate<BlockState> target) {
        BlockState currentState = world.getBlockState(pos);
        return target.test(currentState);
    }

    /**
     * 获取当前矿脉生成器的形状类型。
     *
     * @return 当前的矿脉形状
     */
    VeinShape getShape();

    /**
     * 设置矿脉生成器的形状类型。
     *
     * @param shape 要使用的新矿脉形状
     * @throws IllegalArgumentException 如果形状为null
     */
    void setShape(VeinShape shape);

    /**
     * 获取当前的形状生成器实现。
     *
     * @return 当前的形状生成器
     */
    VeinShapeGenerator getShapeGenerator();

    /**
     * 获取矿脉生成的当前密度值(0.0到1.0)。
     * 密度影响形状内会有多少方块被填充。
     *
     * @return 当前的密度值
     */
    float getDensity();

    /**
     * 设置矿脉生成的密度。
     *
     * @param density 新的密度值(0.0到1.0)
     * @throws IllegalArgumentException 如果密度超出有效范围
     */
    void setDensity(float density);

    /**
     * 获取矿脉生成的最大半径。
     *
     * @return 当前的最大半径(以方块为单位)
     */
    int getMaxRadius();

    /**
     * 设置矿脉生成的最大半径。
     *
     * @param radius 新的最大半径(以方块为单位)
     * @throws IllegalArgumentException 如果半径为负数
     */
    void setMaxRadius(int radius);

    /**
     * 可用的矿脉形状枚举及其对应的生成器。
     * 每种形状都有对应的工厂方法来创建其生成器。
     */
    enum VeinShape {
        /**
         * 具有平滑圆边的球形矿脉。
         */
        SPHERICAL(SphericalGenerator::new),
        
        /**
         * 具有锐利边缘的立方体矿脉。
         */
        CUBIC(CubicGenerator::new),
        
        /**
         * 水平延伸的扁平圆盘状矿脉。
         */
        DISK(DiskGenerator::new),
        
        /**
         * 不规则、有机形状的矿脉。
         */
        IRREGULAR(IrregularGenerator::new),
        
        /**
         * 需要手动配置生成器的自定义形状矿脉。
         */
        CUSTOM(CustomGenerator::new);

        private final ShapeGeneratorFactory factory;

        VeinShape(ShapeGeneratorFactory factory) {
            this.factory = factory;
        }

        /**
         * 为此形状类型创建新的生成器实例。
         *
         * @return 新的形状生成器实例
         */
        public VeinShapeGenerator createGenerator() {
            return factory != null ? factory.create() : null;
        }

        /**
         * 形状生成器工厂的功能接口。
         */
        @FunctionalInterface
        interface ShapeGeneratorFactory {
            /**
             * 创建新的形状生成器实例。
             *
             * @return 新的形状生成器
             */
            VeinShapeGenerator create();
        }
    }

    /**
     * 矿脉形状生成器接口，处理实际的方块放置逻辑。
     */
    interface VeinShapeGenerator {
        /**
         * 在世界中生成矿脉形状的方块。
         *
         * @param world   要生成的世界
         * @param origin  矿脉的中心位置
         * @param radius  矿脉半径(以方块为单位)
         * @param density 填充密度(0.0到1.0)
         * @param target  测试可替换方块的谓词
         * @param state   要放置的方块状态
         * @return 已放置的方块数量
         */
        int generate(WorldAccess world, BlockPos origin, int radius, float density,
                     Predicate<BlockState> target, BlockState state);

        /**
         * 为此生成器添加可以改变生成结果的修饰器。
         * 修饰器将在原始生成后应用。
         *
         * @param modifier 要应用的修饰器
         * @return 应用了修饰器的新生成器
         */
        default VeinShapeGenerator modify(Modifier modifier) {
            return (world, origin, radius, density, target, state) -> {
                int count = generate(world, origin, radius, density, target, state);
                return modifier.modify(world, origin, radius, density, target, state, count);
            };
        }
    }

    /**
     * 矿脉生成修饰器的功能接口，可以在形状生成器初始生成后修改结果。
     */
    @FunctionalInterface
    interface Modifier {
        /**
         * 修改生成的矿脉。
         *
         * @param world        包含矿脉的世界
         * @param origin       矿脉的中心位置
         * @param radius       矿脉半径
         * @param density      生成密度
         * @param target       目标方块谓词
         * @param state        要放置的方块状态
         * @param originalCount 原始生成的方块数量
         * @return 修改后的方块数量
         */
        int modify(WorldAccess world, BlockPos origin, int radius, float density,
                   Predicate<BlockState> target, BlockState state, int originalCount);
    }
}