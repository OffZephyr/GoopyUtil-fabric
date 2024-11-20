package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.networking.nbt_updates.NbtPayloads;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;

public record MoneySyncDataS2CPayload(int credits) implements CustomPayload {
    public static final CustomPayload.Id<MoneySyncDataS2CPayload> ID = new CustomPayload.Id<>(NbtPayloads.S2CMoneyID);
    public static final PacketCodec<PacketByteBuf, MoneySyncDataS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, MoneySyncDataS2CPayload::credits,
            MoneySyncDataS2CPayload::new);


    public static void receive(MoneySyncDataS2CPayload payload, ClientPlayNetworking.Context context){
        ((IEntityDataSaver)context.client().player).getPersistentData().putInt("Credits", payload.credits());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
