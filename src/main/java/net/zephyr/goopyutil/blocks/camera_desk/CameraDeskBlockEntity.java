package net.zephyr.goopyutil.blocks.camera_desk;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.init.BlockEntityInit;

import java.util.ArrayList;
import java.util.List;

public class CameraDeskBlockEntity extends GoopyBlockEntity {
    public static List<BlockPos> posList= new ArrayList<>();
    public CameraDeskBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CAMERA_DESK, pos, state);
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState state, GoopyBlockEntity entity) {
        if (!world.isClient&&!posList.contains(blockPos)) {
            posList.add(blockPos);
        }
        super.tick(world, blockPos, state, entity);
    }

    public float getYaw(){
        return 90;
    }
}
