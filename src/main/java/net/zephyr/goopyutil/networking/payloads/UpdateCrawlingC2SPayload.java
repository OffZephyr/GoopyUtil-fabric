package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.util.mixinAccessing.IPlayerCustomModel;

public record UpdateCrawlingC2SPayload(boolean crawl) implements CustomPayload {
    public static final CustomPayload.Id<UpdateCrawlingC2SPayload> ID = new CustomPayload.Id<>(PayloadDef.C2SCrawlUpdate);
    public static final PacketCodec<RegistryByteBuf, UpdateCrawlingC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, UpdateCrawlingC2SPayload::crawl,
            UpdateCrawlingC2SPayload::new);

    public static void receive(UpdateCrawlingC2SPayload payload, ServerPlayNetworking.Context context){
        ((IPlayerCustomModel)context.player()).setCrawling(payload.crawl());
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
