package net.zephyr.goopyutil.init;

import net.minecraft.text.Text;
import net.zephyr.goopyutil.client.gui.CameraTabletScreen;
import net.zephyr.goopyutil.client.gui.screens.CameraEditScreen;
import net.zephyr.goopyutil.client.gui.screens.PaintbrushScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPBootupScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPDesktopScreen;
import net.zephyr.goopyutil.util.GoopyScreens;

public class ScreensInit {
    public static void init(){
        Text title = Text.empty();
        GoopyScreens.registerScreen("computer_boot", new COMPBootupScreen(title));
        GoopyScreens.registerScreen("paintbrush", new PaintbrushScreen(title));
        GoopyScreens.registerScreen("camera_edit", new CameraEditScreen(title));
        GoopyScreens.registerScreen("camera_tablet", new CameraTabletScreen(title));
    }
}
