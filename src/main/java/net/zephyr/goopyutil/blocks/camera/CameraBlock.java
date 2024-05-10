package net.zephyr.goopyutil.blocks.camera;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlock;
import net.zephyr.goopyutil.init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CameraBlock extends BlockWithEntity {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    public static final BooleanProperty POWERED = LeverBlock.POWERED;
    public static final BooleanProperty CEILING = BooleanProperty.of("ceiling");
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public CameraBlock(Settings settings) {
        super(settings.luminance(Blocks.createLightLevelFromLitBlockState(15)));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CameraBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(LIT, false);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, CEILING, LIT, POWERED);
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            default -> Block.createCuboidShape(6, 6, 0, 10, 16, 12);
            case NORTH -> Block.createCuboidShape(6, 6, 4, 10, 16, 16);
            case EAST -> Block.createCuboidShape(0, 6, 6, 12, 16, 10);
            case WEST -> Block.createCuboidShape(4, 6, 6, 16, 16, 10);
        };
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) { return true;}

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        NbtCompound data = ((CameraBlockEntity)world.getBlockEntity(pos)).getCustomData();
        data.putDouble("pitch", 0);
        data.putDouble("yaw", 0);
        data.putString("Name", "Unnamed Camera");
        data.putBoolean("isUsed", false);
        data.putBoolean("Lit", false);
        data.putBoolean("Flashlight", true);
        data.putBoolean("Active", true);
        data.putByte("NightVision", (byte)1);
        data.putByte("ModeX", (byte)0);
        data.putByte("ModeY", (byte)0);
        data.putDouble("minYaw", -30d);
        data.putDouble("maxYaw", 30d);
        data.putDouble("minPitch", -30d);
        data.putDouble("maxPitch", 30d);
        data.putFloat("panningXProgress", 0);
        data.putFloat("panningYProgress", 0);
        data.putBoolean("panningXReverse", false);
        data.putBoolean("panningYReverse", false);
        data.putByte("yawSpeed", (byte)4);
        data.putByte("pitchSpeed", (byte)4);
        data.putBoolean("Powered", true);
        BlockPos oppositePos = pos.offset(state.get(FACING).getOpposite());
        world.setBlockState(pos, state.with(CEILING, !world.getBlockState(oppositePos).isOpaqueFullCube(world, oppositePos)), 3);
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if(direction == state.get(FACING).getOpposite()){
            world.setBlockState(pos, state.with(CEILING, !neighborState.isOpaqueFullCube(world, neighborPos)), 3);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getPickStack(world, pos, state);
        world.getBlockEntity(pos, BlockEntityInit.LAYERED_BLOCK).ifPresent(blockEntity -> blockEntity.setStackNbt(itemStack));
        return itemStack;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        ItemStack itemStack = new ItemStack(this);
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        blockEntity.setStackNbt(itemStack);
        List<ItemStack> item = new ArrayList<>();
        item.add(itemStack);
        return item;
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {

        return checkType(type, BlockEntityInit.CAMERA,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }
    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(POWERED) && (state.get(FACING) == direction || state.get(CEILING))) {
            return 15;
        }
        return 0;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(POWERED) && (state.get(FACING) == direction || state.get(CEILING))) {
            return 15;
        }
        return 0;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if ((state.get(POWERED) != newState.get(POWERED)) || (state.get(CEILING) != newState.get(CEILING))) {
            this.updateNeighbors(state, world, pos);
        }
        if (moved || state.isOf(newState.getBlock())) {
            return;
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }
    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        for(Direction direction : DIRECTIONS){
            world.updateNeighborsExcept(pos.offset(direction), this, direction.getOpposite());
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        updateNeighbors(state, world, pos);
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }
}
