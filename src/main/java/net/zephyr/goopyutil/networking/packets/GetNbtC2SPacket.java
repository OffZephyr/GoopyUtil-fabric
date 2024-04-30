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

public class GetNbtC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        NbtCompound nbt = buf.readNbt();

        NbtCompound data = nbt.getCompound("data");
        BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
        int id = nbt.getInt("entityID");

        ServerWorld serverWorld = player.getServerWorld();

        PacketByteBuf newBuf = PacketByteBufs.create();
        newBuf.writeNbt(nbt.copy());


        if(nbt.getLong("pos") != 0) {

            if(serverWorld.getWorldChunk(pos).getBlockEntity(pos, WorldChunk.CreationType.IMMEDIATE) instanceof GoopyBlockEntity ent) {
                ent.putCustomData(data.copy());
            }
        }
        else if(nbt.getLong("entityID") != 0) {
            /*if(serverWorld.getEntityById(id) instanceof GoopyEntity ent) {
                ent.putCustomData(data);
            }*/
        }

        for(ServerPlayerEntity p : PlayerLookup.all(server))
            ServerPlayNetworking.send(p, NetChannels.NBT_UPDATE_C, newBuf);

    }
}
