package net.zephyr.goopyutil.networking.nbt_updates;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.networking.nbt_updates.computer.ComputerEjectPayload;
import net.zephyr.goopyutil.networking.nbt_updates.goopy_entity.*;

public class NbtPayloads {
    public static final Identifier C2SBlockUpdate = Identifier.of(GoopyUtil.MOD_ID, "c2s_block_update");
    public static final Identifier S2CBlockUpdatePong = Identifier.of(GoopyUtil.MOD_ID, "s2c_block_update");
    public static final Identifier C2SEntityUpdate = Identifier.of(GoopyUtil.MOD_ID, "c2s_entity_update");
    public static final Identifier S2CEntityUpdatePong = Identifier.of(GoopyUtil.MOD_ID, "s2c_entity_update");
    public static final Identifier C2SItemUpdate = Identifier.of(GoopyUtil.MOD_ID, "c2s_item_update");
    public static final Identifier S2CMoneyID = Identifier.of(GoopyUtil.MOD_ID, "c2s_money_sync");
    public static final Identifier C2SMoneyID = Identifier.of(GoopyUtil.MOD_ID, "s2c_money_sync");
    public static final Identifier S2CJumpscareData = Identifier.of(GoopyUtil.MOD_ID, "s2c_jumpscare_sync");
    public static final Identifier C2SJumpscarePos = Identifier.of(GoopyUtil.MOD_ID, "c2s_jumpscare_sync");
    public static final Identifier C2SComputerEject = Identifier.of(GoopyUtil.MOD_ID, "c2s_computer_eject");
    public static final Identifier C2SAIUpdate = Identifier.of(GoopyUtil.MOD_ID, "c2s_ai_update");
    public static final Identifier S2CAIUpdate = Identifier.of(GoopyUtil.MOD_ID, "s2c_ai_update");
    public static final Identifier C2SCrawlUpdate = Identifier.of(GoopyUtil.MOD_ID, "c2s_crawl_update");
    public static void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(UpdateBlockNbtC2SPayload.ID, UpdateBlockNbtC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateBlockNbtS2CPongPayload.ID, UpdateBlockNbtS2CPongPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateItemNbtC2SPayload.ID, UpdateItemNbtC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateEntityNbtC2SPayload.ID, UpdateEntityNbtC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateEntityNbtS2CPongPayload.ID, UpdateEntityNbtS2CPongPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(UpdateJumpscareDataS2CPayload.ID, UpdateJumpscareDataS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateJumpscarePosC2SPayload.ID, UpdateJumpscarePosC2SPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(AIBehaviorUpdateC2SPayload.ID, AIBehaviorUpdateC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AIBehaviorUpdateS2CPayload.ID, AIBehaviorUpdateS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ComputerEjectPayload.ID, ComputerEjectPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(UpdateCrawlingC2SPayload.ID, UpdateCrawlingC2SPayload.CODEC);
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(UpdateJumpscareDataS2CPayload.ID, UpdateJumpscareDataS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(AIBehaviorUpdateS2CPayload.ID, AIBehaviorUpdateS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(UpdateBlockNbtS2CPongPayload.ID, UpdateBlockNbtS2CPongPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(UpdateEntityNbtS2CPongPayload.ID, UpdateEntityNbtS2CPongPayload::receive);
    }
    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(UpdateBlockNbtC2SPayload.ID, UpdateBlockNbtC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(UpdateItemNbtC2SPayload.ID, UpdateItemNbtC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(UpdateEntityNbtC2SPayload.ID, UpdateEntityNbtC2SPayload::receive);

        ServerPlayNetworking.registerGlobalReceiver(UpdateJumpscarePosC2SPayload.ID, UpdateJumpscarePosC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(AIBehaviorUpdateC2SPayload.ID, AIBehaviorUpdateC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(ComputerEjectPayload.ID, ComputerEjectPayload::receive);

        ServerPlayNetworking.registerGlobalReceiver(UpdateCrawlingC2SPayload.ID, UpdateCrawlingC2SPayload::receive);
    }
}
