package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.networking.PayloadDef;

import java.util.Arrays;

public record UpdateJumpscarePosC2SPayload(NbtCompound nbt) implements CustomPayload {
    public static final Id<UpdateJumpscarePosC2SPayload> ID = new Id<>(PayloadDef.C2SJumpscarePos);
    public static final PacketCodec<RegistryByteBuf, UpdateJumpscarePosC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.NBT_COMPOUND, UpdateJumpscarePosC2SPayload::nbt,
            UpdateJumpscarePosC2SPayload::new);
    public static void receive(UpdateJumpscarePosC2SPayload payload, ServerPlayNetworking.Context context) {
        long[] camPos = payload.nbt().getLongArray("camPos");
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
