package net.deepseek.v1.chainmining;

import net.deepseek.v1.chainmining.data.*;
import net.deepseek.v1.chainmining.enchantments.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class ChainMiningReforgedDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(EnchantmentsGenerator::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModEnchantmentTagProvider::new);
		pack.addProvider(ModModelsProvider::new);
		pack.addProvider(ModRecipesProvider::new);
//		pack.addProvider(ModEnglishLanguageProvider::new);

		DataGenerator.Pack vanillaPack = fabricDataGenerator.createPack();
		vanillaPack.addProvider(ModEquipmentModelProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantments::bootstrap);
		DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
	}
}
