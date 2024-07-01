package net.zephyr.goopyutil.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.util.IEntityDataSaver;

public class GetNbtC2SPayload implements CustomPayload {
    public static final Id<GetNbtC2SPayload> ID = CustomPayload.id("goopyutil_c2s_nbt_get");
    private static NbtCompound nbt = new NbtCompound();
    public static final PacketCodec<PacketByteBuf, GetNbtC2SPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeNbt(value.nbt), buf -> new GetNbtC2SPayload(buf.readNbt()));

    public GetNbtC2SPayload(NbtCompound data){
        nbt = data;
    }
    public static void receive(GetNbtC2SPayload payload, ServerPlayNetworking.Context context){

        NbtCompound data = nbt.getCompound("data");
        BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
        int id = nbt.getInt("entityID");

        ServerWorld serverWorld = context.player().getServerWorld();

        if(nbt.getLong("pos") != 0) {

            if(serverWorld.getBlockEntity(pos) instanceof GoopyBlockEntity ent) {
                ent.putCustomData(data.copy());
                serverWorld.setBlockState(pos, serverWorld.getBlockState(pos), Block.NOTIFY_LISTENERS);
            }
        }
        else if(nbt.getLong("entityID") != 0) {
            ((IEntityDataSaver) serverWorld.getEntityById(id)).getPersistentData().copyFrom(data);
        }
        else if(!nbt.isEmpty()){
            context.player().getMainHandStack().apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                currentNbt.copyFrom(nbt);
            }));
        }

        for(ServerPlayerEntity p : PlayerLookup.all(context.player().getServer())) {
            ServerPlayNetworking.send(p, new SetNbtS2CPayload(nbt));
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
