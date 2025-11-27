package net.deepseek.v1.chainmining.event;

import net.deepseek.v1.chainmining.config.CommonConfig;
import net.deepseek.v1.chainmining.core.keys.ModKeys;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockBreakHandler {
    private final Set<BlockPos> processedBlocks = new HashSet<>();

    public static void triggerChainMining(PlayerEntity player) {
        World world = player.getWorld();
        BlockPos pos = player.getBlockPos();
        BlockState state = world.getBlockState(pos);
        BlockEntity blockEntity = world.getBlockEntity(pos); // 获取 BlockEntity
        ItemStack stack = player.getMainHandStack();

        BlockBreakHandler handler = new BlockBreakHandler();
        handler.afterBlockBreak(world, player, pos, state, blockEntity); // 传递 blockEntity
    }

    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (world.isClient()) return;

        if (!CommonConfig.isEnabled()) return;

        // 获取玩家手中的工具
        ItemStack toolStack = player.getMainHandStack();

        // 检查工具类型
        if (!isToolAllowed(toolStack, CommonConfig.getAllowedTools())) return;

        // 检查按键
        if (!ModKeys.getChainMiningReforgedKey().wasPressed()) return;

        // 检查方块是否允许
        if (!isBlockAllowed(state.getBlock())) return;

        // 执行连锁采集
        processedBlocks.clear();
        mineConnectedBlocks(world, player, pos, state.getBlock(), toolStack, CommonConfig.getMaxChainCount());
    }

    private void mineConnectedBlocks(World world, PlayerEntity player, BlockPos pos, Block targetBlock, ItemStack toolStack, int maxCount) {
        if (maxCount == 0) return;
        if (processedBlocks.contains(pos)) return;

        processedBlocks.add(pos);

        for (BlockPos neighbor : getNeighbors(pos)) {
            BlockState neighborState = world.getBlockState(neighbor);
            if (neighborState.getBlock() == targetBlock) {
                if (tryBreakBlock(world, player, neighbor, toolStack)) {
                    mineConnectedBlocks(world, player, neighbor, targetBlock, toolStack, maxCount - 1);
                }
            }
        }
    }

    private boolean tryBreakBlock(World world, PlayerEntity player, BlockPos pos, ItemStack toolStack) {
        BlockState state = world.getBlockState(pos);
        if (player.canHarvest(state)) {
            world.breakBlock(pos, true, player);
            toolStack.damage((int) CommonConfig.getDurabilityMultiplier(), player,
                    EquipmentSlot.MAINHAND);
            return true;
        }
        return false;
    }

    // 辅助方法 1：判断工具类型是否允许
    private boolean isToolAllowed(ItemStack stack, List<? extends String> allowedTools) {
        if (stack.getItem() instanceof Item) {
            String toolType = stack.getItem().toString().toLowerCase();
            for (String allowed : allowedTools) {
                if (toolType.contains(allowed.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    // 辅助方法 2：判断按键是否按下
    private boolean isKeyPressed(PlayerEntity player, String key) {
        if (key.isEmpty()) return true; // 不需要按键
        KeyBinding binding = KeyBinding.byId(key);
        return binding != null && binding.isPressed();
    }

    // 辅助方法 3：判断方块是否允许
    private boolean isBlockAllowed(Block block) {
        String blockId = Registries.BLOCK.getId(block).toString();
        if (CommonConfig.isUseBlockWhitelist()) {
            // 白名单模式：仅允许列表中的方块
            return CommonConfig.getDefaultAllowedBlocks().contains(blockId) ||
                    CommonConfig.getCustomAllowedBlocks().contains(blockId);
        } else {
            // 黑名单模式：仅禁止列表中的方块
            return !CommonConfig.getDefaultDeniedBlocks().contains(blockId) &&
                    !CommonConfig.getCustomDeniedBlocks().contains(blockId);
        }
    }

    // 获取相邻方块位置
    private BlockPos[] getNeighbors(BlockPos pos) {
        return new BlockPos[]{
                pos.add(1, 0, 0),
                pos.add(-1, 0, 0),
                pos.add(0, 1, 0),
                pos.add(0, -1, 0),
                pos.add(0, 0, 1),
                pos.add(0, 0, -1)
        };
    }
}