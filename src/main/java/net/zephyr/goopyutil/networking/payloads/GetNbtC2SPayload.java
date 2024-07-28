package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;

public record GetNbtC2SPayload(NbtCompound nbt, byte type) implements CustomPayload {
    public static final CustomPayload.Id<GetNbtC2SPayload> ID = new CustomPayload.Id<>(PayloadDef.C2SSetNbtID);
    public static final PacketCodec<RegistryByteBuf, GetNbtC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.NBT_COMPOUND, GetNbtC2SPayload::nbt,
            PacketCodecs.BYTE, GetNbtC2SPayload::type,
            GetNbtC2SPayload::new);

    public static void receive(GetNbtC2SPayload payload, ServerPlayNetworking.Context context){
        NbtCompound nbt = payload.nbt();
        byte type = payload.type();

        NbtCompound data = nbt.getCompound("data");
        ServerWorld serverWorld = context.player().getServerWorld();


        switch (type) {
            case PayloadDef.BLOCK_DATA: {
                BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
                if(serverWorld.getBlockEntity(pos) instanceof GoopyBlockEntity ent) {
                    ent.putCustomData(data.copy());
                    serverWorld.setBlockState(pos, serverWorld.getBlockState(pos), Block.NOTIFY_LISTENERS);
                }

                if(!context.player().getWorld().isClient()) {
                    for (ServerPlayerEntity p : PlayerLookup.all(serverWorld.getServer())) {
                        ServerPlayNetworking.send(p, new SetNbtS2CPayload(nbt, type));
                    }
                }
                break;
            }
            case PayloadDef.ENTITY_DATA: {
                int id = nbt.getInt("entityID");
                if (serverWorld.getEntityById(id) != null) {
                    ((IEntityDataSaver) serverWorld.getEntityById(id)).getPersistentData().copyFrom(data);
                }

                if(!context.player().getWorld().isClient()) {
                    for (ServerPlayerEntity p : PlayerLookup.all(serverWorld.getServer())) {
                        ServerPlayNetworking.send(p, new SetNbtS2CPayload(nbt, type));
                    }
                }
                break;
            }
            case PayloadDef.ITEM_DATA: {
                context.player().getMainHandStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                    currentNbt.copyFrom(nbt);
                }));

                if(!context.player().getWorld().isClient()) {
                    ServerPlayNetworking.send(context.player(), new SetNbtS2CPayload(nbt, type));
                }
                break;
            }
            case PayloadDef.OTHER_DATA: {
                break;
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
