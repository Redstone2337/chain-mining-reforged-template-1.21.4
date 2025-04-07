package net.deepseek.v1.chainmining.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModWorldGenerator extends FabricDynamicRegistryProvider {

    public ModWorldGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    /**
     * @param wrapperLookup The registry wrapper lookup.
     * @param entries The entries to be configured.
     */
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        entries.addAll(wrapperLookup.getOrThrow(RegistryKeys.PLACED_FEATURE));
        entries.addAll(wrapperLookup.getOrThrow(RegistryKeys.CONFIGURED_FEATURE));
    }

    /**
     * @return The name of the provider.
     */
    @Override
    public String getName() {
        return "World Generator";
    }
}
