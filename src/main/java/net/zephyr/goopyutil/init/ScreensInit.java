package net.zephyr.goopyutil.init;

import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import net.zephyr.goopyutil.client.gui.screens.CameraEditScreen;
import net.zephyr.goopyutil.client.gui.screens.EntitySkinScreen;
import net.zephyr.goopyutil.client.gui.screens.arcademachine.ArcademachineScreen;
import net.zephyr.goopyutil.client.gui.screens.PaintbrushScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPBootupScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPDesktopScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPBrowserScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPCodeScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPMusicPlayerScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPRemoteScreen;
import net.zephyr.goopyutil.client.gui.screens.killscreens.DefaultGoopyUtilKillScreen;
import net.zephyr.goopyutil.client.gui.screens.killscreens.ZephyrKillScreen;
import net.zephyr.goopyutil.util.ScreenUtils;

public class ScreensInit {
    public static void init(){
        ScreenUtils.registerScreen("computer_boot", COMPBootupScreen::new);
        ScreenUtils.registerScreen("camera_edit", CameraEditScreen::new);
        ScreenUtils.registerScreen("camera_tablet", CameraTabletScreen::new);
        ScreenUtils.registerScreen("desktop", COMPDesktopScreen::new);
        ScreenUtils.registerScreen("music_player", COMPMusicPlayerScreen::new);
        ScreenUtils.registerScreen("browser", COMPBrowserScreen::new);
        ScreenUtils.registerScreen("remote", COMPRemoteScreen::new);
        ScreenUtils.registerScreen("code", COMPCodeScreen::new);

        ScreenUtils.registerScreen("arcade_machine", ArcademachineScreen::new);

        ScreenUtils.registerScreen("paintbrush", PaintbrushScreen::new);
        ScreenUtils.registerScreen("skins", EntitySkinScreen::new);

        ScreenUtils.registerScreen("death_goopy_default", DefaultGoopyUtilKillScreen::new);
        ScreenUtils.registerScreen("death_goopy", ZephyrKillScreen::new);
    }
}
