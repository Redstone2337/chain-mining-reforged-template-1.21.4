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

    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?,?>> featureRegistry = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(featureRegisterable, OVERWORLD_BEDROCKIUM_ORE_KEY, featureRegistry.getOrThrow(ModConfiguredFeatures.BEDROCKIUM_ORE_OVERWORLD),
                ModOrePlacedFeatures.modifiersWithCount(12, HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
                ModOrePlacedFeatures.createFromVeinGenerator((AdvancedVeinGenerator) generator.getShapeGenerator(),HeightRangePlacementModifier.uniform(YOffset.fixed(-40), YOffset.fixed(40)));

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
