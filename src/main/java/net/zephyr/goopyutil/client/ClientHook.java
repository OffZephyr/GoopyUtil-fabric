package net.zephyr.goopyutil.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.client.gui.screens.BlockEntityScreen;
import net.zephyr.goopyutil.client.gui.screens.EntityScreen;
import net.zephyr.goopyutil.client.gui.screens.GoopyScreen;

public class ClientHook {
    public static void openScreen(Screen screen, BlockPos pos, NbtCompound data){
        if (screen instanceof BlockEntityScreen entityScreen){
            entityScreen.putBlockPos(pos);

            if(!data.isEmpty()) entityScreen.putNbtData(data);
        }
        MinecraftClient.getInstance().setScreen(screen);
    }
    public static void openScreen(Screen screen, int id, NbtCompound data){
        if (screen instanceof EntityScreen entityScreen){
            entityScreen.putEntityID(id);

            if(!data.isEmpty()) entityScreen.putNbtData(data);
        }
        MinecraftClient.getInstance().setScreen(screen);
    }
    public static void openScreen(Screen screen, NbtCompound data){
        if(screen instanceof GoopyScreen goopyScreen) {
            if(!data.isEmpty()) goopyScreen.putNbtData(data);
        }
        MinecraftClient.getInstance().setScreen(screen);
    }
    public static void openScreen(Screen screen){
        if(screen instanceof GoopyScreen goopyScreen) {
            goopyScreen.putNbtData(new NbtCompound());
        }
        MinecraftClient.getInstance().setScreen(screen);
    }

}
