package net.deepseek.v1.chainmining.core.mod;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.deepseek.v1.chainmining.tag.ModBlockTags;
import net.deepseek.v1.chainmining.tag.ModItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;

public class MiningTask extends Thread {
    private final MinecraftServer server;
    private final BlockPos start;
    private final BlockPos end;
    private final BlockPos teleportPos;

    public MiningTask(MinecraftServer server, BlockPos start, BlockPos end, BlockPos teleportPos) {
        this.server = server;
        this.start = start;
        this.end = end;
        this.teleportPos = teleportPos;
    }

    @Override
    public void run() {
        ServerWorld world = server.getOverworld();

        // 计算挖掘范围
        int minX = Math.min(start.getX(), end.getX());
        int maxX = Math.max(start.getX(), end.getX());
        int minZ = Math.min(start.getZ(), end.getZ());
        int maxZ = Math.max(start.getZ(), end.getZ());
        int minY = Math.min(start.getY(), end.getY());
        int maxY = Math.max(start.getY(), end.getY());

        // 计算总方块数量
        int totalBlocks = (maxX - minX + 1) * (maxZ - minZ + 1) * (maxY - minY + 1);

        if (totalBlocks > ChainMiningReforged.MAX_BLOCKS_PER_OPERATION) {
            server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                    "tellraw @a [\"\",{\"text\":\"[VMiner] \",\"color\":\"gold\"},{\"text\":\"错误: 挖矿范围太大! \",\"color\":\"red\"},{\"text\":\"最大允许挖掘 \"},{\"text\":\"" + ChainMiningReforged.MAX_BLOCKS_PER_OPERATION + "\",\"color\":\"yellow\"},{\"text\":\" 个方块，当前范围包含 \"},{\"text\":\"" + totalBlocks + "\",\"color\":\"yellow\"},{\"text\":\" 个方块\"}]");
            ChainMiningReforged.isRunning = false;
            return;
        }

        server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                "tellraw @a [\"\",{\"text\":\"[VMiner] \",\"color\":\"gold\"},{\"text\":\"开始挖矿... \",\"color\":\"green\"},{\"text\":\"总方块数: \"},{\"text\":\"" + totalBlocks + "\",\"color\":\"yellow\"}]");

        // 从下往上挖掘
        int blocksMined = 0;
        for (int y = minY; y <= maxY; y++) {
            if (!ChainMiningReforged.isRunning) {
                break; // 如果挖矿机被停止
            }

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    //Block block = state.getBlock();

                    // 检查方块是否在允许挖掘的标签中
                    if (state.isIn(ModBlockTags.ALLOW_BLOCKS)) {
                        // 挖掘方块
                        List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, null);
                        world.breakBlock(pos, false);
                        blocksMined++;

                        // 每挖掘1000个方块报告一次进度
                        if (blocksMined % 1000 == 0) {
                            int progress = (int) ((blocksMined / (float) totalBlocks) * 100);
                            server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                                    "tellraw @a [\"\",{\"text\":\"[VMiner] \",\"color\":\"gold\"},{\"text\":\"进度: \"},{\"text\":\"" + progress + "%\",\"color\":\"green\"},{\"text\":\" (\"},{\"text\":\"" + blocksMined + "\",\"color\":\"yellow\"},{\"text\":\"/\"},{\"text\":\"" + totalBlocks + "\",\"color\":\"yellow\"},{\"text\":\")\"}]");
                        }

                        // 传送物品到传送方块的随机侧面
                        if (teleportPos != null && world.getBlockState(teleportPos).getBlock() == ModBlocks.TELEPORT_BLOCK) {
                            for (ItemStack stack : drops) {
                                if (stack.getRegistryEntry().isIn(ModItemTags.SUPPORTED_ITEMS)) {
                                    // 随机选择一个侧面传送物品
                                    Direction side = ChainMiningReforged.getRandomSide();
                                    BlockPos spawnPos = teleportPos.offset(side);
                                    Block.dropStack(world, spawnPos, stack);
                                }
                            }
                        }
                    }

                    // 稍微延迟以防止服务器卡顿
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }

        // 挖矿完成
        server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                "tellraw @a [\"\",{\"text\":\"[VMiner] \",\"color\":\"gold\"},{\"text\":\"挖矿完成! \",\"color\":\"green\"},{\"text\":\"总共挖掘 \"},{\"text\":\"" + blocksMined + "\",\"color\":\"yellow\"},{\"text\":\" 个方块\"}]");
        ChainMiningReforged.isRunning = false;
    }
}