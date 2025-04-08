package net.deepseek.v1.chainmining.world.gen;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.core.worlds.ModOrePlacedFeatures;
import net.deepseek.v1.chainmining.impl.AdvancedVeinGenerator;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;


import java.util.List;

public class ModPlacedFeatures {
    private static final AdvancedVeinGenerator generator = new AdvancedVeinGenerator();

    public static final RegistryKey<PlacedFeature> OVERWORLD_BEDROCKIUM_ORE_KEY = of("overworld_bedrockium_ore");
    public static final RegistryKey<PlacedFeature> OVERWORLD_BEDROCKIUM_ORE_KEY_MEDIUM = of("overworld_bedrockium_ore_medium");
    public static final RegistryKey<PlacedFeature> OVERWORLD_BEDROCKIUM_ORE_KEY_LARGE = of("overworld_bedrockium_ore_large");

//    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_SMALL = ConfiguredFeatures.of("ore_diamond_small");
//    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_MEDIUM = ConfiguredFeatures.of("ore_diamond_medium");
//    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_LARGE = ConfiguredFeatures.of("ore_diamond_large");

//        public static final RegistryKey<PlacedFeature> ORE_DIAMOND_MEDIUM = PlacedFeatures.of("ore_diamond_medium");
//        public static final RegistryKey<PlacedFeature> ORE_DIAMOD_LARGE =N PlacedFeatures.of("ore_diamond_large");


    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?,?>> featureRegistry = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

//        register(featureRegisterable,
//                OVERWORLD_BEDROCKIUM_ORE_KEY,
//                featureRegistry.getOrThrow(ModConfiguredFeatures.BEDROCKIUM_ORE_OVERWORLD),
//                ModOrePlacedFeatures.modifiersWithCount(12, HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
//                ModOrePlacedFeatures.createFromVeinGenerator((AdvancedVeinGenerator) generator.getShapeGenerator(),HeightRangePlacementModifier.uniform(YOffset.fixed(-40), YOffset.fixed(40)));
//
//        register(
//                featureRegisterable,
//                OVERWORLD_BEDROCKIUM_ORE_KEY,
//                featureRegistry.getOrThrow(ModConfiguredFeatures.BEDROCKIUM_ORE_OVERWORLD),
//                ModOrePlacedFeatures.modifiersWithCount(7, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-80), YOffset.aboveBottom(80)))
//        );
        register(
                featureRegisterable,
                OVERWORLD_BEDROCKIUM_ORE_KEY,
                featureRegistry.getOrThrow(ModConfiguredFeatures.BEDROCKIUM_ORE_OVERWORLD_SMALL),
                ModOrePlacedFeatures.modifiersWithCount(7, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-80), YOffset.aboveBottom(80)))
        );
        register(
                featureRegisterable,
                OVERWORLD_BEDROCKIUM_ORE_KEY_MEDIUM,
                featureRegistry.getOrThrow(ModConfiguredFeatures.BEDROCKIUM_ORE_OVERWORLD_MEDIUM),
                ModOrePlacedFeatures.modifiersWithCount(2, HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(-4)))
        );
        register(
                featureRegisterable,
                OVERWORLD_BEDROCKIUM_ORE_KEY_LARGE,
                featureRegistry.getOrThrow(ModConfiguredFeatures.BEDROCKIUM_ORE_OVERWORLD_LARGE),
                ModOrePlacedFeatures.modifiersWithRarity(9, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-80), YOffset.aboveBottom(80)))
        );
    }


    public static RegistryKey<PlacedFeature> of(String id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(ChainMiningReforged.MOD_ID, id));
    }
    public static void register(
            Registerable<PlacedFeature> featureRegisterable,
            RegistryKey<PlacedFeature> key,
            RegistryEntry<ConfiguredFeature<?, ?>> feature,
            List<PlacementModifier> modifiers
    ) {
        featureRegisterable.register(key, new PlacedFeature(feature, List.copyOf(modifiers)));
    }
}
