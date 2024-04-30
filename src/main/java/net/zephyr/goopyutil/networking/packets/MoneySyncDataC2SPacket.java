package net.zephyr.goopyutil.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.networking.NetChannels;
import net.zephyr.goopyutil.util.IEntityDataSaver;

public class MoneySyncDataC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        for(ServerPlayerEntity p : PlayerLookup.all(server)) {
            PacketByteBuf buf1 = PacketByteBufs.create();
            buf1.writeInt(((IEntityDataSaver)p).getPersistentData().getInt("Credits"));
            ServerPlayNetworking.send(p, NetChannels.MONEY_SYNC, buf1);
        }

    }
}
