package net.zephyr.goopyutil.blocks.layered_block;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;

public class LayeredBlockEntity extends GoopyBlockEntity {
    public LayeredBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.LAYERED_BLOCK, pos, state);
    }
}
