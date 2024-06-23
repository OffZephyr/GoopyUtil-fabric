package net.zephyr.goopyutil.client.gui.screens;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public abstract class BlockEntityScreen extends GoopyScreen {

    BlockPos blockPos = new BlockPos(0, 0, 0);
    public void putBlockPos(BlockPos pos){
        blockPos = pos;
    }
    public BlockPos getBlockPos(){
        return blockPos;
    }
    public BlockEntityScreen(Text title) {
        super(title);
    }
}
