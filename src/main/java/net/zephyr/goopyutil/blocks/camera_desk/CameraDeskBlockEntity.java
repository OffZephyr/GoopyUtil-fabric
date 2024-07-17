package net.zephyr.goopyutil.blocks.camera_desk;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.init.BlockEntityInit;

public class CameraDeskBlockEntity extends GoopyBlockEntity {
    public CameraDeskBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CAMERA_DESK, pos, state);
    }
}
