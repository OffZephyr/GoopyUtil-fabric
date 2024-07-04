package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.client.ClientHook;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.util.GoopyScreens;

public record SetScreenS2CPayload(String index, NbtCompound data, byte type) implements CustomPayload {
    public static final CustomPayload.Id<SetScreenS2CPayload> ID = new CustomPayload.Id<>(PayloadDef.ScreenPackedID);
    public static final PacketCodec<RegistryByteBuf, SetScreenS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SetScreenS2CPayload::index,
            PacketCodecs.NBT_COMPOUND, SetScreenS2CPayload::data,
            PacketCodecs.BYTE, SetScreenS2CPayload::type,
            SetScreenS2CPayload::new);

    public static void receive(SetScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            if (context.player() instanceof ClientPlayerEntity) {
                String index = payload.index();
                byte type = payload.type();
                NbtCompound nbt = payload.data();
                NbtCompound data = nbt.getCompound("data");

                if (GoopyScreens.getScreens().containsKey(index)) {

                    switch (type) {
                        case PayloadDef.BLOCK_DATA -> {
                            long pos = nbt.getLong("pos");
                            ClientHook.openScreen(index, data, pos);
                        }
                        case PayloadDef.ENTITY_DATA -> {
                            int id = nbt.getInt("entityID");
                            ClientHook.openScreen(index, data, id);
                        }
                        default -> ClientHook.openScreen(index, data, 0);
                    }
                }
            }
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
