package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.client.ClientHook;
import net.zephyr.goopyutil.util.GoopyScreens;

public class SetScreenS2CPayload implements CustomPayload {
    public static final Id<SetScreenS2CPayload> ID = CustomPayload.id("goopyutil_s2c_screen_open");

    public static NbtCompound nbt = new NbtCompound();
    public static final PacketCodec<PacketByteBuf, SetScreenS2CPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeNbt(value.nbt), buf -> new SetScreenS2CPayload(buf.readNbt()));

    public SetScreenS2CPayload(NbtCompound data){
        nbt = data;
    }

    public static void receive(SetScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        if(context.player() instanceof ClientPlayerEntity) {
            System.out.println("received");
            String index = nbt.getString("index");
            BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
            int id = nbt.getInt("entityID");
            NbtCompound data = nbt.getCompound("data");
            Screen screen;
            if (GoopyScreens.getScreens().containsKey(index)) {
                screen = GoopyScreens.getScreens().get(index);

                if (nbt.getLong("pos") != 0) {
                    context.client().execute(() -> {
                        ClientHook.openScreen(screen, pos, data);
                    });
                } else if (nbt.getLong("entityID") != 0) {
                    context.client().execute(() -> {
                        ClientHook.openScreen(screen, id, data);
                    });
                } else if (!data.isEmpty()) {
                    context.client().execute(() -> {
                        ClientHook.openScreen(screen, data);
                    });
                } else {
                    context.client().execute(() -> {
                        ClientHook.openScreen(screen);
                    });
                }
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
