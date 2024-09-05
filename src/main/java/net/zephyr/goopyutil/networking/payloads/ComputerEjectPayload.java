package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.computer.ComputerBlock;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.networking.PayloadDef;

public record ComputerEjectPayload(long blockPos) implements CustomPayload {
    public static final CustomPayload.Id<ComputerEjectPayload> ID = new CustomPayload.Id<>(PayloadDef.C2SComputerEject);
    public static final PacketCodec<RegistryByteBuf, ComputerEjectPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_LONG, ComputerEjectPayload::blockPos,
            ComputerEjectPayload::new);


    public static void receive(ComputerEjectPayload payload, ServerPlayNetworking.Context context) {
        BlockPos pos = BlockPos.fromLong(payload.blockPos());
        World world = context.player().getWorld();
        BlockState state = world.getBlockState(pos);
        if(state.isOf(BlockInit.COMPUTER)) {
            ((ComputerBlock)state.getBlock()).ejectFloppy(world, pos);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
