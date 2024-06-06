package net.zephyr.goopyutil.util;

import com.mojang.datafixers.types.Type;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.client.gui.screens.GoopyScreen;
import net.zephyr.goopyutil.networking.NetChannels;

import java.util.HashMap;
import java.util.Map;

public class GoopyScreens {
    public static Map<String, Screen> ScreenList = new HashMap<>();

    public static void registerScreen(String id, Screen screen){
        ScreenList.put(id, screen);
    }
    public static Map<String, Screen> getScreens(){
        return ScreenList;
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, BlockPos blockPos){
        openScreenOnServer(player, screenIndex, blockPos, new NbtCompound());
    }

    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, int entityID){
        openScreenOnServer(player, screenIndex, entityID, new NbtCompound());
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, BlockPos blockPos, NbtCompound data){
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.putString("index", screenIndex);
        nbt.putLong("pos", blockPos.asLong());
        nbt.put("data", data);
        buf.writeNbt(nbt);

        ServerPlayNetworking.send(player, NetChannels.SCREEN_PACKET_ID, buf);
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, int entityID, NbtCompound data){
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.putString("index", screenIndex);
        nbt.putInt("entityID", entityID);
        nbt.put("data", data);
        buf.writeNbt(nbt);
        ServerPlayNetworking.send(player, NetChannels.SCREEN_PACKET_ID, buf);
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, NbtCompound data){
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.putString("index", screenIndex);
        nbt.put("data", data);
        buf.writeNbt(nbt);
        ServerPlayNetworking.send(player, NetChannels.SCREEN_PACKET_ID, buf);
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex){
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.putString("index", screenIndex);
        buf.writeNbt(nbt);
        ServerPlayNetworking.send(player, NetChannels.SCREEN_PACKET_ID, buf);
    }


    public static void saveNbtFromScreen(NbtCompound data, BlockPos pos){
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.put("data", data);
        nbt.putLong("pos", pos.asLong());
        buf.writeNbt(nbt);

        ClientPlayNetworking.send(NetChannels.NBT_UPDATE_S, buf);
    }

    public static void saveNbtFromScreen(NbtCompound data, int entityID){
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.put("data", data);
        nbt.putInt("entityID", entityID);
        buf.writeNbt(nbt);

        ClientPlayNetworking.send(NetChannels.NBT_UPDATE_S, buf);
    }

    public static void saveNbtFromScreen(NbtCompound data){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(data);

        ClientPlayNetworking.send(NetChannels.NBT_UPDATE_S, buf);
    }

}
