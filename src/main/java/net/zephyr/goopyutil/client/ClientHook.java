package net.zephyr.goopyutil.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.zephyr.goopyutil.util.GoopyScreens;

public class ClientHook {
    public static void openScreen(String index, NbtCompound nbt, long l){
        if (GoopyScreens.getScreens().containsKey(index)) {
            Screen screen = GoopyScreens.getScreens().get(index).create(Text.translatable("screen." + index + ".title"), nbt, l);
            if(MinecraftClient.getInstance().currentScreen == null || MinecraftClient.getInstance().currentScreen.getClass() != screen.getClass()) {
                MinecraftClient.getInstance().setScreen(screen);
            }
        }
    }

}
