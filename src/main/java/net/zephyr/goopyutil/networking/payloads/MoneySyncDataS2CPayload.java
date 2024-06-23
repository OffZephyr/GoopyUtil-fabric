package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.goopyutil.util.IEntityDataSaver;

public class MoneySyncDataS2CPayload implements CustomPayload {
    public static final CustomPayload.Id<MoneySyncDataS2CPayload> ID = CustomPayload.id("goopyutil_s2c_money_sync");
    public static final PacketCodec<PacketByteBuf, MoneySyncDataS2CPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeInt(value.credits), buf -> new MoneySyncDataS2CPayload(buf.readInt()));
    public static int credits = 0;
    public MoneySyncDataS2CPayload(int money){
        credits = money;
    }


    public static void receive(MoneySyncDataS2CPayload payload, ClientPlayNetworking.Context context){
        ((IEntityDataSaver)context.client().player).getPersistentData().putInt("Credits", credits);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
