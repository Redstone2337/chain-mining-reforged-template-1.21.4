package net.deepseek.v1.chainmining;

import net.deepseek.v1.chainmining.data.EnchantmentsGenerator;
import net.deepseek.v1.chainmining.data.ModEnchantmentTagProvider;
import net.deepseek.v1.chainmining.data.ModEnglishLanguageProvider;
import net.deepseek.v1.chainmining.data.ModItemTagProvider;
import net.deepseek.v1.chainmining.enchantments.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class ChainMiningReforgedDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(EnchantmentsGenerator::new);
//		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModEnchantmentTagProvider::new);
//		pack.addProvider(ModEnglishLanguageProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantments::bootstrap);
		DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
	}
}
