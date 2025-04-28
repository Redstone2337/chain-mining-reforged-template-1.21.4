package net.deepseek.v1.chainmining.items;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.List;

public record ModToolMaterial(
        TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems
) {
//    public static final ToolMaterial WOOD = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 59, 2.0F, 0.0F, 15, ItemTags.WOODEN_TOOL_MATERIALS);
//    public static final ToolMaterial STONE = new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 5, ItemTags.STONE_TOOL_MATERIALS);
//    public static final ToolMaterial IRON = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 6.0F, 2.0F, 14, ItemTags.IRON_TOOL_MATERIALS);
//    public static final ToolMaterial DIAMOND = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1561, 8.0F, 3.0F, 10, ItemTags.DIAMOND_TOOL_MATERIALS);
//    public static final ToolMaterial GOLD = new ToolMaterial(BlockTags.INCORRECT_FOR_GOLD_TOOL, 32, 12.0F, 0.0F, 22, ItemTags.GOLD_TOOL_MATERIALS);
//    public static final ToolMaterial NETHERITE = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0F, 4.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);

    public static final ToolMaterial BEDROCK = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1024, 6.0F, 5.0F, 12, ItemTags.NETHERITE_TOOL_MATERIALS);;
    public static final ToolMaterial BEDROCKIUM = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2048, 8.0F, 10.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);;

    private Item.Settings applyBaseSettings(Item.Settings settings) {
        return settings.maxDamage(this.durability).repairable(this.repairItems).enchantable(this.enchantmentValue);
    }

    public Item.Settings applyToolSettings(Item.Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return this.applyBaseSettings(settings)
                .component(
                        DataComponentTypes.TOOL,
                        new ToolComponent(
                                List.of(
                                        ToolComponent.Rule.ofNeverDropping(registryEntryLookup.getOrThrow(this.incorrectBlocksForDrops)),
                                        ToolComponent.Rule.ofAlwaysDropping(registryEntryLookup.getOrThrow(effectiveBlocks), this.speed)
                                ),
                                1.0F,
                                1
                        )
                )
                .attributeModifiers(this.createToolAttributeModifiers(attackDamage, attackSpeed));
    }

    private AttributeModifiersComponent createToolAttributeModifiers(float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage + this.attackDamageBonus, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }

    public Item.Settings applySwordSettings(Item.Settings settings, float attackDamage, float attackSpeed) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return this.applyBaseSettings(settings)
                .component(
                        DataComponentTypes.TOOL,
                        new ToolComponent(
                                List.of(
                                        ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(Blocks.COBWEB.getRegistryEntry()), 15.0F),
                                        ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)
                                ),
                                1.0F,
                                2
                        )
                )
                .attributeModifiers(this.createSwordAttributeModifiers(attackDamage, attackSpeed));
    }

    private AttributeModifiersComponent createSwordAttributeModifiers(float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage + this.attackDamageBonus, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }
}


//public record ModToolMaterial(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems) {
//    public static final ToolMaterial BEDROCK;
//    public static final ToolMaterial BEDROCKIUM;
////    public static final ToolMaterial VAMPIRE;
////    public static final ToolMaterial SLOWNESS;
////    public static final ToolMaterial BLUEXI;
//
//
//    public ModToolMaterial(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems) {
//        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
//        this.durability = durability;
//        this.speed = speed;
//        this.attackDamageBonus = attackDamageBonus;
//        this.enchantmentValue = enchantmentValue;
//        this.repairItems = repairItems;
//    }
//
//    private Item.Settings applyBaseSettings(Item.Settings settings) {
//        return settings.maxDamage(this.durability).repairable(this.repairItems).enchantable(this.enchantmentValue);
//    }
//
//    public Item.Settings applyToolSettings(Item.Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed) {
//        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
//        return this.applyBaseSettings(settings).component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofNeverDropping(registryEntryLookup.getOrThrow(this.incorrectBlocksForDrops)), ToolComponent.Rule.ofAlwaysDropping(registryEntryLookup.getOrThrow(effectiveBlocks), this.speed)), 1.0F, 1)).attributeModifiers(this.createToolAttributeModifiers(attackDamage, attackSpeed));
//    }
//
//    private AttributeModifiersComponent createToolAttributeModifiers(float attackDamage, float attackSpeed) {
//        return AttributeModifiersComponent.builder().add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, (double)(attackDamage + this.attackDamageBonus), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, (double)attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
//    }
//
//    public Item.Settings applySwordSettings(Item.Settings settings, float attackDamage, float attackSpeed) {
//        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
//        return this.applyBaseSettings(settings).component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(new RegistryEntry[]{Blocks.COBWEB.getRegistryEntry()}), 15.0F), ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)), 1.0F, 2)).attributeModifiers(this.createSwordAttributeModifiers(attackDamage, attackSpeed));
//    }
//
//    private AttributeModifiersComponent createSwordAttributeModifiers(float attackDamage, float attackSpeed) {
//        return AttributeModifiersComponent.builder().add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, (double)(attackDamage + this.attackDamageBonus), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, (double)attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
//    }
//
//    public TagKey<Block> incorrectBlocksForDrops() {
//        return this.incorrectBlocksForDrops;
//    }
//
//    public int durability() {
//        return this.durability;
//    }
//
//    public float speed() {
//        return this.speed;
//    }
//
//    public float attackDamageBonus() {
//        return this.attackDamageBonus;
//    }
//
//    public int enchantmentValue() {
//        return this.enchantmentValue;
//    }
//
//    public TagKey<Item> repairItems() {
//        return this.repairItems;
//    }
//
//    static {
//        BEDROCK = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1024, 6.0F, 5.0F, 12, ItemTags.NETHERITE_TOOL_MATERIALS);
//        BEDROCKIUM = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2048, 8.0F, 10.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);
////        VAMPIRE = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2048, 8.0F, 10.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);
////        SLOWNESS = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 4096, 10.0F, 15.0F, 34, ItemTags.NETHERITE_TOOL_MATERIALS);
////        BLUEXI = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2025, 5.5F, 5.5F, 14, ItemTags.NETHERITE_TOOL_MATERIALS);
//    }
//}

