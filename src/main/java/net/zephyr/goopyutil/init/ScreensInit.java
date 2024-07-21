package net.zephyr.goopyutil.init;

import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import net.zephyr.goopyutil.client.gui.screens.CameraEditScreen;
import net.zephyr.goopyutil.client.gui.screens.MINTYTEA.ArcademachineScreen;
import net.zephyr.goopyutil.client.gui.screens.PaintbrushScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPBootupScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPDesktopScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPBrowserScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPCodeScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPMusicPlayerScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPRemoteScreen;
import net.zephyr.goopyutil.util.GoopyScreens;

public class ScreensInit {
    public static void init(){
        GoopyScreens.registerScreen("computer_boot", COMPBootupScreen::new);
        GoopyScreens.registerScreen("paintbrush", PaintbrushScreen::new);
        GoopyScreens.registerScreen("camera_edit", CameraEditScreen::new);
        GoopyScreens.registerScreen("camera_tablet", CameraTabletScreen::new);
        GoopyScreens.registerScreen("desktop", COMPDesktopScreen::new);
        GoopyScreens.registerScreen("music_player", COMPMusicPlayerScreen::new);
        GoopyScreens.registerScreen("browser", COMPBrowserScreen::new);
        GoopyScreens.registerScreen("remote", COMPRemoteScreen::new);
        GoopyScreens.registerScreen("code", COMPCodeScreen::new);
        GoopyScreens.registerScreen("arcade_machine", ArcademachineScreen::new);
    }
}
