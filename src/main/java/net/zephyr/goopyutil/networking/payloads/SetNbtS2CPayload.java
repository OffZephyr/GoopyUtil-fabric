package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.util.IEntityDataSaver;

public class SetNbtS2CPayload implements CustomPayload {
    public static final Id<SetNbtS2CPayload> ID = CustomPayload.id("goopyutil_s2c_nbt_set");
    public static final PacketCodec<PacketByteBuf, SetNbtS2CPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeNbt(value.nbt), buf -> new SetNbtS2CPayload(buf.readNbt()));
    private static NbtCompound nbt = new NbtCompound();

    public SetNbtS2CPayload(NbtCompound data){
        nbt = data;
    }
    public static void receive(SetNbtS2CPayload payload, ClientPlayNetworking.Context context) {
        NbtCompound data = nbt.getCompound("data");

        BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
        int id = nbt.getInt("entityID");

        World world = context.client().world;

        if (nbt.getLong("pos") != 0) {
            context.client().execute(() -> {
                if (world.getWorldChunk(pos).getBlockEntity(pos) instanceof GoopyBlockEntity ent) {
                    world.setBlockState(pos, world.getBlockState(pos), Block.NOTIFY_LISTENERS);
                    ent.putCustomData(data);
                }
            });
        } else if (nbt.getLong("entityID") != 0) {
            context.client().execute(() -> {
                ((IEntityDataSaver) world.getEntityById(id)).getPersistentData().copyFrom(data);
            });
        } else if (!nbt.isEmpty()) {
            context.client().execute(() -> {
                context.player().getMainHandStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                    currentNbt.copyFrom(nbt);
                }));
            });
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
