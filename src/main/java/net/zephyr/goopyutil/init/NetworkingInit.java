package net.zephyr.goopyutil.init;

import net.zephyr.goopyutil.networking.nbt_updates.NbtPayloads;
import net.zephyr.goopyutil.networking.screens.ScreenPayloads;

public class NetworkingInit {
    public static void registerClientReceivers(){
        ScreenPayloads.registerClientReceivers();
        NbtPayloads.registerClientReceivers();
    }
    public static void registerServerReceivers(){
        ScreenPayloads.registerServerReceivers();
        NbtPayloads.registerServerReceivers();
    }
    public static void registerPayloads(){
        ScreenPayloads.registerPayloads();
        NbtPayloads.registerPayloads();
    }
}
