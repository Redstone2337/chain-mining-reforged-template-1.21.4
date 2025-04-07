package net.deepseek.v1.chainmining.core.worlds;

import net.deepseek.v1.chainmining.impl.AdvancedVeinGenerator;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModOrePlacedFeatures {

    public static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    public static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(count), heightModifier);
    }

    public static List<PlacementModifier> modifiersWithRarity(int chance, PlacementModifier heightModifier) {
        return modifiers(RarityFilterPlacementModifier.of(chance), heightModifier);
    }

    public static List<PlacementModifier> createFromVeinGenerator(
            AdvancedVeinGenerator generator, PlacementModifier baseHeight
    ) {
        // 根据密度动态计算数量或稀有度
        int count = (int) (generator.getDensity() * 15);
        // 根据半径调整高度范围
        PlacementModifier height = HeightRangePlacementModifier.uniform(
                YOffset.fixed(-generator.getMaxRadius()), YOffset.fixed(generator.getMaxRadius()));
        return modifiers(CountPlacementModifier.of(count), height);
    }
}

