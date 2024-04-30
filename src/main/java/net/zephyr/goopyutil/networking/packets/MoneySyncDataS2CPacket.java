package net.zephyr.goopyutil.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.zephyr.goopyutil.util.IEntityDataSaver;

public class MoneySyncDataS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        ((IEntityDataSaver)client.player).getPersistentData().putInt("Credits", buf.readInt());
    }
}
