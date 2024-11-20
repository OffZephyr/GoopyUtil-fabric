package net.zephyr.goopyutil.blocks.plushies;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.init.BlockEntityInit;

public class BephPlushieBlockEntity extends BlockEntity {
    public BephPlushieBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.BEPH_PLUSHIE, pos, state);
    }
}
