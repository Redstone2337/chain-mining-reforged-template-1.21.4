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
    public static final TagKey<Item> ALL = of("all_mod_tag");
    public static final TagKey<Item> BEDROCK_TOOL_MATERIALS = of("bedrock_tool_materials");
    public static final TagKey<Item> BEDROCKIUM_TOOL_MATERIALS = of("bedrockium_tool_materials");
    public static final TagKey<Item> REPAIRS_BEDROCK_ARMOR = of("repairs_bedrock_armor");
    public static final TagKey<Item> REPAIRS_BEDROCKIUM_ARMOR = of("repairs_bedrockium_armor");
    public static final TagKey<Item> SUPPORTED_ITEMS = of("supported_items");

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(ChainMiningReforged.MOD_ID, id));
    }

    public static void register() {
        ChainMiningReforged.LOGGER.info("注册标签成功！");
    }
}
