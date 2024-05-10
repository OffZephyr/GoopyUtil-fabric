package net.zephyr.goopyutil.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.networking.NetChannels;

public class GoopyEntity extends Entity {
    NbtCompound customData = new NbtCompound();
    String NbtIndex = GoopyUtil.MOD_ID;
    public NbtCompound getCustomData(){
        return customData;
    }
    public void putCustomData(NbtCompound nbt){
        customData = nbt.copy();
    }
    public void setNbtIndex(String index){
        NbtIndex = index;
    }
    public GoopyEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
    public void updateEntityData(NbtCompound Nbt, MinecraftClient client, int ID){
        NbtCompound pack = new NbtCompound();
        pack.put("data", Nbt);
        pack.putInt("entityID", ID);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(pack);
            ClientPlayNetworking.send(NetChannels.NBT_UPDATE_S, buf);
    }
    public void updateEntityData(NbtCompound Nbt, MinecraftServer server, int ID){
        NbtCompound pack = new NbtCompound();
        pack.put("data", Nbt);
        pack.putInt("entityID", ID);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(pack);
        for (ServerPlayerEntity p : PlayerLookup.all(server)){
            ServerPlayNetworking.send(p, NetChannels.NBT_UPDATE_C, buf);
        }
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        if(nbt.contains(NbtIndex)){
            customData = nbt.getCompound(NbtIndex).copy();
        }
        super.readNbt(nbt);
    }
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put(NbtIndex, customData);
        return super.writeNbt(nbt);
    }
}
