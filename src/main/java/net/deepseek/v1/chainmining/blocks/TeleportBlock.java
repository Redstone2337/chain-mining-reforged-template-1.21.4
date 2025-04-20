package net.deepseek.v1.chainmining.blocks;

import com.mojang.serialization.MapCodec;
import net.deepseek.v1.chainmining.blocks.entities.ModBlockEntities;
import net.deepseek.v1.chainmining.blocks.entities.TeleportingBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TeleportBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final EnumProperty<Direction> FACING = Properties.FACING;
    public static final MapCodec<TeleportBlock> CODEC = createCodec(TeleportBlock::new);
    public static final VoxelShape SHAPE = Block.createCuboidShape(0,0,0,16,16,16);
//    public static final BooleanProperty SINGLE = BooleanProperty.of("single");
//    public static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
//    public static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
//    public static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
//    public static final VoxelShape EAST_SHAPE = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);


    protected TeleportBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP));
    }

//    @Override
//    protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
//        boolean type = state.get(SINGLE);
//        Direction direction = state.get(FACING);
//        VoxelShape voxelShape;
//
//        if (type) {
//            switch (direction) {
//                case WEST -> voxelShape = WEST_SHAPE.asCuboid();
//                case EAST -> voxelShape = EAST_SHAPE.asCuboid();
//                case SOUTH -> voxelShape = SOUTH_SHAPE.asCuboid();
//                case NORTH -> voxelShape = NORTH_SHAPE.asCuboid();
//                default -> throw new MatchException(null, null);
//            }
//
//            return voxelShape;
//        } else {
//            return VoxelShapes.fullCube();
//        }
//    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(/*SINGLE,*/FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
//        BlockPos pos = ctx.getBlockPos();
//        Direction direction = ctx.getHorizontalPlayerFacing();
//        BlockState state = ctx.getWorld().getBlockState(pos);
//        BlockState state2 = Objects.requireNonNull(super.getPlacementState(ctx));
//
//        if (state.isOf(this) && state.get(FACING) == ctx.getSide().getOpposite()) {
//            return state.isOf(this) ? state2.with(SINGLE, false) : super.getPlacementState(ctx);
//        }
//
//        if (direction == Direction.NORTH && ctx.getHitPos().z - pos.getZ() > 0.5) {
//            return state2.with(FACING, Direction.SOUTH).with(SINGLE, true);
//        } else if (direction == Direction.SOUTH && ctx.getHitPos().z - pos.getZ() < 0.5) {
//            return state2.with(FACING, Direction.NORTH).with(SINGLE, true);
//        } else if (direction == Direction.WEST && ctx.getHitPos().x - pos.getX() > 0.5) {
//            return state2.with(FACING, Direction.EAST).with(SINGLE, true);
//        } else if (direction == Direction.EAST && ctx.getHitPos().x - pos.getX() < 0.5) {
//            return state2.with(FACING, Direction.WEST).with(SINGLE, true);
//        } else {
//            return state2.with(FACING, direction);
//        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TeleportingBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.TELEPORTING_BLOCK, TeleportingBlockEntity::tick);
    }

    // 新增的 checkType 方法
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(
            BlockEntityType<A> givenType,
            BlockEntityType<E> expectedType,
            BlockEntityTicker<? super E> ticker
    ) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}