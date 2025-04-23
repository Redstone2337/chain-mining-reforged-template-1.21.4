package net.deepseek.v1.chainmining.core.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;

public record CustomBrewingRecipe(
        String type,
        RegistryEntry<Potion> input,      // 输入药水/物品
        Ingredient addition,    // 酿造材料
        RegistryEntry<Potion> output     // 输出药水/物品
) {
    // 定义 Codec 序列化规则
    public static final Codec<CustomBrewingRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("type").forGetter(CustomBrewingRecipe::type),
                    Potion.CODEC.fieldOf("input").forGetter(CustomBrewingRecipe::input),
                    Ingredient.CODEC.fieldOf("addition").forGetter(CustomBrewingRecipe::addition),
                    Potion.CODEC.fieldOf("output").forGetter(CustomBrewingRecipe::output)
            ).apply(instance, CustomBrewingRecipe::new)
    );
}
