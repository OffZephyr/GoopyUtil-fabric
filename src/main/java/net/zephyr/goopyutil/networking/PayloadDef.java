package net.zephyr.goopyutil.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.networking.payloads.*;

public class PayloadDef {
    public static final byte BLOCK_DATA = 0;
    public static final byte ENTITY_DATA = 1;
    public static final byte ITEM_DATA = 2;
    public static final byte OTHER_DATA = 3;

    public static final Identifier ScreenPackedID = Identifier.of(GoopyUtil.MOD_ID, "s2c_screen_open");
    public static final Identifier S2CSetNbtID = Identifier.of(GoopyUtil.MOD_ID, "s2c_nbt_set");
    public static final Identifier C2SSetNbtID = Identifier.of(GoopyUtil.MOD_ID, "c2s_nbt_set");
    public static final Identifier S2CMoneyID = Identifier.of(GoopyUtil.MOD_ID, "c2s_money_sync");
    public static final Identifier C2SMoneyID = Identifier.of(GoopyUtil.MOD_ID, "s2c_money_sync");

    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(GetNbtC2SPayload.ID, GetNbtC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetNbtS2CPayload.ID, SetNbtS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(MoneySyncDataC2SPayload.ID, MoneySyncDataC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MoneySyncDataS2CPayload.ID, MoneySyncDataS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetScreenS2CPayload.ID, SetScreenS2CPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(GetNbtC2SPayload.ID, GetNbtC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(MoneySyncDataC2SPayload.ID, MoneySyncDataC2SPayload::receive);
    }
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SetNbtS2CPayload.ID, SetNbtS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(MoneySyncDataS2CPayload.ID, MoneySyncDataS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(SetScreenS2CPayload.ID, SetScreenS2CPayload::receive);
    }
}
