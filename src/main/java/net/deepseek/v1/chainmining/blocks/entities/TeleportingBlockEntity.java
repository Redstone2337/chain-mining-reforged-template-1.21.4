package net.deepseek.v1.chainmining.blocks.entities;

import net.deepseek.v1.chainmining.blocks.TeleportBlock;
import net.deepseek.v1.chainmining.tag.ModItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class TeleportingBlockEntity extends BlockEntity {
    private static final Random RANDOM = new Random();

    public TeleportingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TELEPORTING_BLOCK, pos, state);
    }

//    public static void tick(World world, BlockPos pos, BlockState state, TeleportingBlockEntity blockEntity) {
//        if (world.isClient()) return;
//
//        // 精确检测上方0.5x0.5区域
//        Box detectionBox = new Box(
//                pos.getX() + 0.25, pos.getY() + 1.0, pos.getZ() + 0.25,
//                pos.getX() + 0.75, pos.getY() + 1.1, pos.getZ() + 0.75
//        );
//
//        // 检查方块上方的物品实体
//        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class,
//               detectionBox,
//                itemEntity -> true
//        );
//        for (ItemEntity itemEntity : items) {
//            ItemStack stack = itemEntity.getStack();
//            Item item = stack.getItem();
//
//            // 检查物品是否在支持标签中
//            if (stack.isIn(SUPPORTED_ITEMS)) {
//                teleportItem(world, pos, state, itemEntity);
//            }
//        }
//    }

    // 在方块实体的 tick() 方法中
    public static void tick(World world, BlockPos pos, BlockState state, TeleportingBlockEntity blockEntity) {
        if (world.isClient) return;

        // 精确检测上方0.5x0.5区域
        Box detectionBox = new Box(
                pos.getX() + 0.25, pos.getY() + 1.0, pos.getZ() + 0.25,
                pos.getX() + 0.75, pos.getY() + 1.1, pos.getZ() + 0.75
        );

        List<ItemEntity> items = world.getEntitiesByClass(
                ItemEntity.class,
                detectionBox,
                e -> e.getStack().isIn(ModItemTags.SUPPORTED_ITEMS) // 检查标签
        );

        items.forEach(item -> teleportItem(world, pos, state, item));
    }

    private static void teleportItem(World world, BlockPos pos, BlockState state, ItemEntity itemEntity) {
        Direction facing = state.get(TeleportBlock.FACING);
        Direction[] possibleDirections = {
                Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN
        };

        // 随机选择一个方向 (排除当前朝向)
        Direction targetDirection;
        do {
            targetDirection = possibleDirections[RANDOM.nextInt(possibleDirections.length)];
        } while (targetDirection == facing);

        // 计算传送位置
        BlockPos targetPos = pos.offset(targetDirection);
        Vec3d targetCenter = Vec3d.ofCenter(targetPos);

        // 传送物品
        itemEntity.teleport((ServerWorld) world, targetCenter.getX(), targetCenter.getY(), targetCenter.getZ(),null, itemEntity.getYaw(), itemEntity.getPitch(),false);

        // 播放粒子效果
        world.addParticle(
                ParticleTypes.PORTAL,
                pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                (targetDirection.getOffsetX()) * 0.1,
                (targetDirection.getOffsetY()) * 0.1,
                (targetDirection.getOffsetZ()) * 0.1
        );
    }
}
