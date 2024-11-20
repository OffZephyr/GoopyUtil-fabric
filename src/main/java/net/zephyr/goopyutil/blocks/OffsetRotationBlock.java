package net.zephyr.goopyutil.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class OffsetRotationBlock extends BlockWithEntity {
    public static final int offset_grid_size = 16;
    public static boolean drawingOutline = false;
    public static final float angleSnap = 22.5f;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty OFFSET_X = IntProperty.of("x_offset", 0, offset_grid_size);
    public static final IntProperty OFFSET_Y = IntProperty.of("y_offset", 0, offset_grid_size);
    public static final IntProperty OFFSET_Z = IntProperty.of("z_offset", 0, offset_grid_size);

    protected OffsetRotationBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        double x = ctx.getHitPos().getX() - ctx.getBlockPos().getX();
        double z = ctx.getHitPos().getZ() - ctx.getBlockPos().getZ();

        x = Math.clamp(x, 0, 1);
        z = Math.clamp(z, 0, 1);

        if(ctx.getPlayer().isSneaking()){
            x = 0.5f;
            z = 0.5f;
        }
        return getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(OFFSET_X, (int)(x * offset_grid_size))
                .with(OFFSET_Y, 0)
                .with(OFFSET_Z, (int)(z * offset_grid_size));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(world.getBlockEntity(pos) != null && placer != null) {

            float rotation = placer.getHeadYaw();
            if(placer.isSneaking()){
                rotation = Math.round(rotation / angleSnap) * angleSnap;
            }
            ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putFloat("Rotation", rotation);
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }
    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
}
