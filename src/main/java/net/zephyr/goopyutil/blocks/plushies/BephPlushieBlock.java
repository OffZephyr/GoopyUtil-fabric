package net.zephyr.goopyutil.blocks.plushies;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.OffsetRotationBlock;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BephPlushieBlock extends OffsetRotationBlock {
    public BephPlushieBlock(Settings settings) {
        super(settings);
    }


    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0.25, 0.0, 0.35, 0.75, 0.5, 0.70)));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0.25, 0.5, 0.25, 0.75, 1, 0.75)));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0.4, 1, 0.4, 0.6, 1.25, 0.6)));
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) { return true;}


    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return true;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BephPlushieBlockEntity(pos, state);
    }
}
