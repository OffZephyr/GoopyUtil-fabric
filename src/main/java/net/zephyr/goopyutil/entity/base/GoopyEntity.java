package net.zephyr.goopyutil.entity.base;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.networking.payloads.GetNbtC2SPayload;
import net.zephyr.goopyutil.networking.payloads.SetNbtS2CPayload;

public class GoopyEntity extends Entity {
    NbtCompound customData = new NbtCompound();
    String NbtIndex = GoopyUtil.MOD_ID;
    public GoopyEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

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
