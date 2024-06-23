package net.zephyr.goopyutil.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.networking.payloads.*;

public class PayloadDef {


    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(GetNbtC2SPayload.ID, GetNbtC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(GetNbtC2SPayload.ID, GetNbtC2SPayload::receive);

        PayloadTypeRegistry.playC2S().register(MoneySyncDataC2SPayload.ID, MoneySyncDataC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(MoneySyncDataC2SPayload.ID, MoneySyncDataC2SPayload::receive);
    }
    public static void registerS2CPackets() {
        PayloadTypeRegistry.playS2C().register(SetNbtS2CPayload.ID, SetNbtS2CPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(SetNbtS2CPayload.ID, SetNbtS2CPayload::receive);

        PayloadTypeRegistry.playS2C().register(MoneySyncDataS2CPayload.ID, MoneySyncDataS2CPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(MoneySyncDataS2CPayload.ID, MoneySyncDataS2CPayload::receive);

        PayloadTypeRegistry.playS2C().register(SetScreenS2CPayload.ID, SetScreenS2CPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(SetScreenS2CPayload.ID, SetScreenS2CPayload::receive);
    }
}
