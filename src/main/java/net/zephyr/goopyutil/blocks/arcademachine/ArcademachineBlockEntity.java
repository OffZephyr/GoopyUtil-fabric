package net.zephyr.goopyutil.blocks.arcademachine;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.init.BlockEntityInit;

import java.util.Objects;

public class ArcademachineBlockEntity extends GoopyBlockEntity {
    public ArcademachineBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ARCADE_MACHINE, pos, state);
    }

}
