package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.zephyr.goopyutil.util.IEntityDataSaver;

public class MoneySyncDataC2SPayload implements CustomPayload {
    public static final CustomPayload.Id<MoneySyncDataC2SPayload> ID = CustomPayload.id("goopyutil_c2s_money_sync");
    public static final PacketCodec<PacketByteBuf, MoneySyncDataC2SPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeInt(value.credits), buf -> new MoneySyncDataC2SPayload(buf.readInt()));
    public static int credits = 0;

    public MoneySyncDataC2SPayload(int money){
        credits = money;
    }

    public static void receive(MoneySyncDataC2SPayload payload, ServerPlayNetworking.Context context){
        for(ServerPlayerEntity p : PlayerLookup.all(context.player().server)) {
            PacketByteBuf buf1 = PacketByteBufs.create();
            buf1.writeInt(((IEntityDataSaver)p).getPersistentData().getInt("Credits"));
            ServerPlayNetworking.send(p, new MoneySyncDataS2CPayload(credits));
        }

    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
