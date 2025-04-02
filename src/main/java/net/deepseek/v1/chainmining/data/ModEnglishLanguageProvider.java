package net.deepseek.v1.chainmining.data;

import net.deepseek.v1.chainmining.enchantments.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.addEnchantment(ModEnchantments.FIRE_FOR_ONESELF,"Fire One Oneself");
        translationBuilder.addEnchantment(ModEnchantments.ONE_PUSH,"One Push");
        translationBuilder.addEnchantment(ModEnchantments.ICE_BLEND,"Ice Blend");
        translationBuilder.addEnchantment(ModEnchantments.FIRE_BLEND,"Fire Blend");
        translationBuilder.addEnchantment(ModEnchantments.ICE_FIRE_BLEND,"Ice Fire Blend");
    }
}
