package net.zephyr.goopyutil.client.gui.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.networking.NetChannels;

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
