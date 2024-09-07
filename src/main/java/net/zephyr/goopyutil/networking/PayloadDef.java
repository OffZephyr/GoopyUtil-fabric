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
    public static final byte BOOL_AI_DATA = 0;
    public static final byte INT_AI_DATA = 1;
    public static final byte BLOCK_POS_AI_DATA = 2;
    public static final byte STRING_AI_DATA = 3;

    public static final Identifier ScreenPackedID = Identifier.of(GoopyUtil.MOD_ID, "s2c_screen_open");
    public static final Identifier S2CSetNbtID = Identifier.of(GoopyUtil.MOD_ID, "s2c_nbt_set");
    public static final Identifier C2SSetNbtID = Identifier.of(GoopyUtil.MOD_ID, "c2s_nbt_set");
    public static final Identifier S2CMoneyID = Identifier.of(GoopyUtil.MOD_ID, "c2s_money_sync");
    public static final Identifier C2SMoneyID = Identifier.of(GoopyUtil.MOD_ID, "s2c_money_sync");
    public static final Identifier S2CJumpscareData = Identifier.of(GoopyUtil.MOD_ID, "s2c_jumpscare_sync");
    public static final Identifier C2SJumpscarePos = Identifier.of(GoopyUtil.MOD_ID, "c2s_jumpscare_sync");
    public static final Identifier C2SComputerEject = Identifier.of(GoopyUtil.MOD_ID, "c2s_computer_eject");
    public static final Identifier C2SAIUpdate = Identifier.of(GoopyUtil.MOD_ID, "c2s_ai_update");
    public static final Identifier S2CAIUpdate = Identifier.of(GoopyUtil.MOD_ID, "s2c_ai_update");

    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(GetNbtC2SPayload.ID, GetNbtC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetNbtS2CPayload.ID, SetNbtS2CPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(MoneySyncDataC2SPayload.ID, MoneySyncDataC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MoneySyncDataS2CPayload.ID, MoneySyncDataS2CPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SetScreenS2CPayload.ID, SetScreenS2CPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(UpdateJumpscareDataS2CPayload.ID, UpdateJumpscareDataS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateJumpscarePosC2SPayload.ID, UpdateJumpscarePosC2SPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(AIBehaviorUpdateC2SPayload.ID, AIBehaviorUpdateC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AIBehaviorUpdateS2CPayload.ID, AIBehaviorUpdateS2CPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(ComputerEjectPayload.ID, ComputerEjectPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(GetNbtC2SPayload.ID, GetNbtC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(MoneySyncDataC2SPayload.ID, MoneySyncDataC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(UpdateJumpscarePosC2SPayload.ID, UpdateJumpscarePosC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(ComputerEjectPayload.ID, ComputerEjectPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(AIBehaviorUpdateC2SPayload.ID, AIBehaviorUpdateC2SPayload::receive);
    }
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SetNbtS2CPayload.ID, SetNbtS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(MoneySyncDataS2CPayload.ID, MoneySyncDataS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(SetScreenS2CPayload.ID, SetScreenS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(UpdateJumpscareDataS2CPayload.ID, UpdateJumpscareDataS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(AIBehaviorUpdateS2CPayload.ID, AIBehaviorUpdateS2CPayload::receive);
    }
}
