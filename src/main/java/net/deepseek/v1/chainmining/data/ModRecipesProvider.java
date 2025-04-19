package net.deepseek.v1.chainmining.data;

import net.deepseek.v1.chainmining.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends FabricRecipeProvider {
    public ModRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {
                // Add your recipe generation logic here
                // For example, you can create a custom recipe like this:
                // ShapedRecipeBuilder.create(ModItems.BEDROCK_SWORD)
                //         .pattern(" X ")
                //         .pattern(" X ")
                //         .pattern(" Y ")
                //         .input('X', ModItems.BEDROCK)
                //         .input('Y', Items.STICK)
                //         .criterion("has_item", conditionsFromItem(ModItems.BEDROCK))
                //         .offerTo(this);
                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCK_SWORD)
                        .pattern(" X ")
                        .pattern(" X ")
                        .pattern(" Y ")
                        .input('X', ModItems.BEDROCK_STONE)
                        .input('Y', ModItems.DIAMOND_STICK)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCK_STONE))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCK_PICKAXE)
                        .pattern("XXX")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .input('X', ModItems.BEDROCK_STONE)
                        .input('Y', ModItems.DIAMOND_STICK)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCK_STONE))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCK_AXE)
                        .pattern("XX ")
                        .pattern("XY ")
                        .pattern(" Y ")
                        .input('X', ModItems.BEDROCK_STONE)
                        .input('Y', ModItems.DIAMOND_STICK)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCK_STONE))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCK_SHOVEL)
                        .pattern(" X ")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .input('X', ModItems.BEDROCK_STONE)
                        .input('Y', ModItems.DIAMOND_STICK)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCK_STONE))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCK_HOE)
                        .pattern("XX ")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .input('X', ModItems.BEDROCK_STONE)
                        .input('Y', ModItems.DIAMOND_STICK)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCK_STONE))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCKIUM_SWORD)
                        .pattern(" X ")
                        .pattern(" X ")
                        .pattern(" Y ")
                        .input('X', ModItems.BEDROCKIUM_STONE)
                        .input('Y', Items.STICK)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCKIUM_STONE))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCKIUM_PICKAXE)
                        .pattern("XXX")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .input('X', ModItems.BEDROCKIUM_STONE)
                        .input('Y', Items.STICK)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCKIUM_STONE))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCKIUM_AXE)
                        .pattern("XX ")
                        .pattern("XY ")
                        .pattern(" Y ")
                        .input('X', ModItems.BEDROCKIUM_STONE)
                        .input('Y', Items.STICK)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCKIUM_STONE))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCKIUM_SHOVEL)
                        .pattern(" X ")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .input('X', ModItems.BEDROCKIUM_STONE)
                        .input('Y', Items.STICK)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCKIUM_STONE))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCKIUM_HOE)
                        .pattern("XX ")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .input('X', ModItems.BEDROCKIUM_STONE)
                        .input('Y', Items.STICK)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCKIUM_STONE))
                        .offerTo(recipeExporter);

                createShapeless(RecipeCategory.MISC,ModItems.BEDROCK_STONE)
                        .input(DefaultCustomIngredients.any(
                                Ingredient.ofItem(Items.BEDROCK)
                        ))
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCKIUM_STONE))
                        .offerTo(recipeExporter);

               createShapeless(RecipeCategory.MISC,ModItems.DIAMOND_STICK,2)
                       .input(DefaultCustomIngredients.any(
                               Ingredient.ofItem(Items.DIAMOND), Ingredient.ofItem(Items.STICK),
                               Ingredient.ofItem(Items.DIAMOND)
                               )
                       )
                        .criterion("has_item", conditionsFromItem(Items.STICK))
                        .offerTo(recipeExporter);

//                createShapeless(RecipeCategory.MISC,ModItems.BEDROCKIUM_STONE)
//                        .input(DefaultCustomIngredients.any(
//                                Ingredient.ofItem(ModItems.BEDROCK_STONE), Ingredient.ofItem(ModItems.BEDROCK_STONE)
//                            )
//                        )
//                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCK_STONE))
//                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.MISC,ModItems.BEDROCKIUM_STONE)
                        .pattern(" X ")
                        .pattern("XYX")
                        .pattern(" X ")
                        .input('X', ModItems.BEDROCK_STONE)
                        .input('Y', Items.NETHERITE_SCRAP)
                        .criterion("has_item", conditionsFromItem(ModItems.BEDROCK_STONE))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.BEDROCKIUM_BOW,1)
                        .pattern(" AB")
                        .pattern("A B")
                        .pattern(" AB")
                        .input('A',ModItems.DIAMOND_STICK)
                        .input('B',Items.STRING)
                        .criterion("has_item",conditionsFromItem(ModItems.DIAMOND_STICK))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.MISC,ModItems.BEDROCKIUM_ARROW,1)
                        .pattern("AAA")
                        .pattern("ABA")
                        .pattern("AAA")
                        .input('A',ModItems.BEDROCKIUM_STONE)
                        .input('B',Items.ARROW)
                        .criterion("has_item",conditionsFromItem(ModItems.BEDROCKIUM_STONE))
                        .offerTo(recipeExporter);

                createShapeless(RecipeCategory.MISC,ModItems.TRPPED_BEDROCKIUM_ARROW,4)
                        .input(DefaultCustomIngredients.any(
                                Ingredient.ofItem(ModItems.BEDROCK_STONE),
                                Ingredient.ofItem(ModItems.BEDROCK_STONE),
                                Ingredient.ofItem(ModItems.BEDROCK_STONE),
                                Ingredient.ofItem(ModItems.BEDROCKIUM_STONE),
                                Ingredient.ofItem(ModItems.BEDROCKIUM_ARROW)
                        ))
                        .criterion("has_bedrockium_arrow",conditionsFromItem(ModItems.BEDROCKIUM_ARROW))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.COMBAT,ModItems.ICE_CLAW,1)
                        .pattern("ABC")
                        .pattern("DEF")
                        .pattern(" G ")
                        .input('A',Items.IRON_SWORD)
                        .input('B',Items.GOLDEN_SWORD)
                        .input('C',Items.DIAMOND_SWORD)
                        .input('D',Items.STONE_SWORD)
                        .input('E',Items.WOODEN_SWORD)
                        .input('F',Items.NETHERITE_SWORD)
                        .input('G',ModItems.DIAMOND_STICK)
                        .criterion("has_diamond_stick",conditionsFromItem(ModItems.DIAMOND_STICK))
                        .offerTo(recipeExporter);
            }
        };
    }

    @Override
    public String getName() {
        return "";
    }
}
