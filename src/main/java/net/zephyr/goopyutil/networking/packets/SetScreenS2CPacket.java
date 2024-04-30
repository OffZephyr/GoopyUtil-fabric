package net.zephyr.goopyutil.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.client.ClientHook;
import net.zephyr.goopyutil.util.GoopyScreens;

public class SetScreenS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        String index = nbt.getString("index");
        BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
        int id = nbt.getInt("entityID");
        NbtCompound data = nbt.getCompound("data");
        Screen screen;
        if(GoopyScreens.getScreens().containsKey(index)) {
            screen = GoopyScreens.getScreens().get(index);

            if (nbt.getLong("pos") != 0) {
                client.execute(() -> {
                    ClientHook.openScreen(screen, pos, data);
                });
            } else if (nbt.getLong("entityID") != 0) {
                client.execute(() -> {
                    ClientHook.openScreen(screen, id, data);
                });
            } else if(!data.isEmpty()){
                client.execute(() -> {
                    ClientHook.openScreen(screen, data);
                });
            } else {
                client.execute(() -> {
                    ClientHook.openScreen(screen);
                });
            }
        }
    }
}
