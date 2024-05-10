package net.zephyr.goopyutil.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.entity.GoopyEntity;

public class UpdateNbtS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        NbtCompound nbt = buf.readNbt();

        NbtCompound data = nbt.getCompound("data");

        BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
        int id = nbt.getInt("entityID");

        World world = client.world;

        if(nbt.getLong("pos") != 0) {
            client.execute(() -> {
                if(world.getBlockEntity(pos) instanceof GoopyBlockEntity ent) {
                    ent.putCustomData(data);
                }
            });
        }
        else if(nbt.getLong("entityID") != 0){
            client.execute(() -> {
                if(world.getEntityById(id) instanceof GoopyEntity ent) {
                    ent.putCustomData(data);
                }
            });
        }
        else if(!nbt.isEmpty()){
            client.execute(() -> {
                client.player.getMainHandStack().setNbt(nbt);
            });
        }
    }
}
