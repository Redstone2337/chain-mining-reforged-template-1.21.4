package net.deepseek.v1.chainmining.core.recipes.loaders;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.core.recipes.CustomBrewingRecipe;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.Reader;

public class BrewingRecipeLoader implements SimpleSynchronousResourceReloadListener {
    // 定义数据包路径：对应 data/<modid>/brewing_recipes/*.json
    private static final String PATH = "brewing";

    @Override
    public void reload(ResourceManager manager) {
        for (var entry : manager.findResources(PATH, id -> id.getPath().endsWith(".json")).entrySet()) {
            Identifier fileId = entry.getKey();

            manager.getResource(fileId).ifPresentOrElse(resource -> {
                try (Reader reader = resource.getReader()) {
                    JsonElement json = JsonParser.parseReader(reader);
                    CustomBrewingRecipe recipe = CustomBrewingRecipe.CODEC.parse(JsonOps.INSTANCE, json)
                            .getOrThrow();

                    // 检查 type 是否匹配
                    if (!"cmr:brewing".equals(recipe.type())) {
                        System.err.println("Skipping recipe (wrong type): " + fileId);
                        return;
                    }

                    // 注册配方
                    FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
                        builder.registerPotionRecipe(
                                recipe.input(),
                                recipe.addition(),
                                recipe.output()
                        );
                    });

                    System.out.println("Loaded brewing recipe: " + fileId);
                } catch (Exception e) {
                    System.err.println("Failed to load recipe " + fileId + ": " + e.getMessage());
                }
            }, () -> System.err.println("Missing resource: " + fileId));
        }
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(ChainMiningReforged.MOD_ID,"brewing_recipe_loader");
    }
}
