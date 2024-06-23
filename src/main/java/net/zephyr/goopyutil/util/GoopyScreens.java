package net.zephyr.goopyutil.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.networking.payloads.GetNbtC2SPayload;
import net.zephyr.goopyutil.networking.payloads.SetScreenS2CPayload;

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
        NbtCompound nbt = new NbtCompound();
        nbt.putString("index", screenIndex);
        nbt.putLong("pos", blockPos.asLong());
        nbt.put("data", data);

        ServerPlayNetworking.send(player, new SetScreenS2CPayload(nbt));
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, int entityID, NbtCompound data){
        NbtCompound nbt = new NbtCompound();
        nbt.putString("index", screenIndex);
        nbt.putInt("entityID", entityID);
        nbt.put("data", data);

        ServerPlayNetworking.send(player, new SetScreenS2CPayload(nbt));
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, NbtCompound data){
        NbtCompound nbt = new NbtCompound();
        nbt.putString("index", screenIndex);
        nbt.put("data", data);

        System.out.println("sent");
        ServerPlayNetworking.send(player, new SetScreenS2CPayload(nbt));
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex){
        NbtCompound nbt = new NbtCompound();
        nbt.putString("index", screenIndex);

        ServerPlayNetworking.send(player, new SetScreenS2CPayload(nbt));
    }


    public static void saveNbtFromScreen(NbtCompound data, BlockPos pos){
        NbtCompound nbt = new NbtCompound();
        nbt.put("data", data);
        nbt.putLong("pos", pos.asLong());

        ClientPlayNetworking.send(new GetNbtC2SPayload(nbt));
    }

    public static void saveNbtFromScreen(NbtCompound data, int entityID){
        NbtCompound nbt = new NbtCompound();
        nbt.put("data", data);
        nbt.putInt("entityID", entityID);

        ClientPlayNetworking.send(new GetNbtC2SPayload(nbt));
    }

    public static void saveNbtFromScreen(NbtCompound data){
        ClientPlayNetworking.send(new GetNbtC2SPayload(data));
    }

}
