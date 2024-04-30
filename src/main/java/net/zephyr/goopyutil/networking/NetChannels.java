package net.zephyr.goopyutil.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.networking.packets.*;

public class NetChannels {
    public static final Identifier SCREEN_PACKET_ID = new Identifier(GoopyUtil.MOD_ID, "s2c_screen_open");
    public static final Identifier NBT_UPDATE_S = new Identifier(GoopyUtil.MOD_ID, "c2s_entity_save");
    public static final Identifier NBT_UPDATE_C = new Identifier(GoopyUtil.MOD_ID, "s2c_entity_save");
    public static final Identifier MONEY_SYNC = new Identifier(GoopyUtil.MOD_ID, "s2c_money_sync");
    public static final Identifier MONEY_SYNC_CALL = new Identifier(GoopyUtil.MOD_ID, "c2s_money_sync");


    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(NBT_UPDATE_S, UpdateNbtC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(MONEY_SYNC_CALL, MoneySyncDataC2SPacket::receive);
    }
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(NBT_UPDATE_C, UpdateNbtS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SCREEN_PACKET_ID, SetScreenS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(MONEY_SYNC, MoneySyncDataS2CPacket::receive);
    }
}
