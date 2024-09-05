package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.networking.PayloadDef;

public record UpdateJumpscareDataS2CPayload(int entityID) implements CustomPayload {
    public static final CustomPayload.Id<UpdateJumpscareDataS2CPayload> ID = new CustomPayload.Id<>(PayloadDef.S2CJumpscareData);
    public static final PacketCodec<RegistryByteBuf, UpdateJumpscareDataS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UpdateJumpscareDataS2CPayload::entityID,
            UpdateJumpscareDataS2CPayload::new);
    public static void receive(UpdateJumpscareDataS2CPayload payload, ClientPlayNetworking.Context context) {
        if(context.client().world.getEntityById(payload.entityID()) instanceof GoopyUtilEntity entity){
            GoopyUtilEntity.jumpscareEntity = entity;
        }
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
