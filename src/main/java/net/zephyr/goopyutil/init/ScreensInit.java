package net.zephyr.goopyutil.init;

import net.zephyr.goopyutil.client.gui.screens.CameraEditScreen;
import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import net.zephyr.goopyutil.client.gui.screens.EntitySkinScreen;
import net.zephyr.goopyutil.client.gui.screens.PaintbrushScreen;
import net.zephyr.goopyutil.client.gui.screens.arcademachine.ArcademachineScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPBootupScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPDesktopScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPCodeScreen;
import net.zephyr.goopyutil.client.gui.screens.killscreens.DefaultGoopyUtilKillScreen;
import net.zephyr.goopyutil.client.gui.screens.killscreens.ZephyrKillScreen;
import net.zephyr.goopyutil.util.GoopyNetworkingUtils;

public class ScreensInit {
    public static final String CAMERA_TABLET = "camera_tablet";
    public static final String COMPUTER_BOOT = "computer_boot";
    public static final String COMPUTER_DESKTOP = "desktop";
    public static final String COMPUTER_CODE = "code";
    public static final String ARCADE_MACHINE = "arcade_machine";
    public static final String CAMERA_EDIT = "camera_edit";
    public static final String PAINTBRUSH = "paintbrush";
    public static final String SKINS = "skins";
    public static final String DEFAULT_KILLSCREEN = "death_goopy_default";
    public static final String ZEPHYR_KILLSCREEN = "death_goopy";
    public static void init(){
        GoopyNetworkingUtils.registerScreen(CAMERA_TABLET, CameraTabletScreen::new);

        GoopyNetworkingUtils.registerScreen(COMPUTER_BOOT, COMPBootupScreen::new);
        GoopyNetworkingUtils.registerScreen(COMPUTER_DESKTOP, COMPDesktopScreen::new);
        GoopyNetworkingUtils.registerScreen(COMPUTER_CODE, COMPCodeScreen::new);

        GoopyNetworkingUtils.registerScreen(ARCADE_MACHINE, ArcademachineScreen::new);

        GoopyNetworkingUtils.registerScreen(CAMERA_EDIT, CameraEditScreen::new);
        GoopyNetworkingUtils.registerScreen(PAINTBRUSH, PaintbrushScreen::new);
        GoopyNetworkingUtils.registerScreen(SKINS, EntitySkinScreen::new);

        GoopyNetworkingUtils.registerScreen(DEFAULT_KILLSCREEN, DefaultGoopyUtilKillScreen::new);
        GoopyNetworkingUtils.registerScreen(ZEPHYR_KILLSCREEN, ZephyrKillScreen::new);


        /*

        ScreenUtils.registerScreen("music_player", COMPMusicPlayerScreen::new);
        ScreenUtils.registerScreen("browser", COMPBrowserScreen::new);
        ScreenUtils.registerScreen("remote", COMPRemoteScreen::new);
         */
    }
}
