package net.deepseek.v1.chainmining.world.gen;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?,?>> BEDROCKIUM_ORE_OVERWORLD_SMALL = of("bedrockium_ore_overworld_small");
    public static final RegistryKey<ConfiguredFeature<?,?>> BEDROCKIUM_ORE_OVERWORLD_MEDIUM = of("bedrockium_ore_overworld_medium");
    public static final RegistryKey<ConfiguredFeature<?,?>> BEDROCKIUM_ORE_OVERWORLD_LARGE = of("bedrockium_ore_overworld_large");



//    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_SMALL = ConfiguredFeatures.of("ore_diamond_small");
//    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_MEDIUM = ConfiguredFeatures.of("ore_diamond_medium");
//    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_LARGE = ConfiguredFeatures.of("ore_diamond_large");


    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        RuleTest stoneReplace = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplace = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> overworldTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplace, ModBlocks.BEDROCKIUM_ORE.getDefaultState())
                , OreFeatureConfig.createTarget(deepslateReplace, ModBlocks.BEDROCKIUM_ORE.getDefaultState())
        );

        register(featureRegisterable, BEDROCKIUM_ORE_OVERWORLD_SMALL, Feature.ORE, new OreFeatureConfig(overworldTargets, 4, 0.5F));
        register(featureRegisterable, BEDROCKIUM_ORE_OVERWORLD_MEDIUM, Feature.ORE, new OreFeatureConfig(overworldTargets, 12, 0.7F));
        register(featureRegisterable, BEDROCKIUM_ORE_OVERWORLD_LARGE, Feature.ORE, new OreFeatureConfig(overworldTargets, 8, 1.0F));

    }

    public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(ChainMiningReforged.MOD_ID, id));
    }

    public static <FC extends FeatureConfig, F extends Feature<FC>> void register(
            Registerable<ConfiguredFeature<?, ?>> registerable, RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC config
    ) {
        registerable.register(key, new ConfiguredFeature<FC, F>(feature, config));
    }
}
