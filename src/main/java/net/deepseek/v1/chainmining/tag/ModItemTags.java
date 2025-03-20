package net.deepseek.v1.chainmining.tag;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags {


    public static final TagKey<Item> FUNCTION_LIST = of("function_list");
    public static final TagKey<Item> PICKAXE = of("pickaxe");
    public static final TagKey<Item> SWORD = of("sword");

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(ChainMiningReforged.MOD_ID, id));
    }

    public static void register() {
        ChainMiningReforged.LOGGER.info("注册标签成功！");
    }
}
