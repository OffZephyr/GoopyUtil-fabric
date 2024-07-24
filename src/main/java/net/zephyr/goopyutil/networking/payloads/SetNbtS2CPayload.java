package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;

public record SetNbtS2CPayload(NbtCompound nbt, byte type) implements CustomPayload {
    public static final CustomPayload.Id<SetNbtS2CPayload> ID = new CustomPayload.Id<>(PayloadDef.S2CSetNbtID);
    public static final PacketCodec<RegistryByteBuf, SetNbtS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.NBT_COMPOUND, SetNbtS2CPayload::nbt,
            PacketCodecs.BYTE, SetNbtS2CPayload::type,
            SetNbtS2CPayload::new);
    public static void receive(SetNbtS2CPayload payload, ClientPlayNetworking.Context context) {
        NbtCompound nbt = payload.nbt();
        byte type = payload.type();

        NbtCompound data = nbt.getCompound("data");
        World world = context.player().getWorld();

        switch (type) {
            case PayloadDef.BLOCK_DATA: {
                BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
                context.client().execute(() -> {
                    if (world.getWorldChunk(pos).getBlockEntity(pos) instanceof GoopyBlockEntity ent) {
                        world.setBlockState(pos, world.getBlockState(pos), Block.NOTIFY_LISTENERS);
                        ent.putCustomData(data);
                    }
                });
                break;
            }
            case PayloadDef.ENTITY_DATA: {
                int id = nbt.getInt("entityID");
                context.client().execute(() -> {
                    if (world.getEntityById(id) != null) {
                        ((IEntityDataSaver) world.getEntityById(id)).getPersistentData().copyFrom(data);
                    }
                });
                break;
            }
            case PayloadDef.ITEM_DATA: {
                context.client().execute(() -> {
                    context.player().getMainHandStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                        currentNbt.copyFrom(nbt);
                    }));
                });
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
