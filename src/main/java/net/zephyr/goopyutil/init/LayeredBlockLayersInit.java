package net.zephyr.goopyutil.init;

import net.minecraft.text.Text;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockLayers;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPBootupScreen;
import net.zephyr.goopyutil.util.GoopyScreens;

public class LayeredBlockLayersInit {
    public static void init(){
        LayeredBlockLayers.registerLayer("goopyutil_demo_checker", 2, false);
        LayeredBlockLayers.registerLayer("goopyutil_demo_walltiles", 3, false);
        LayeredBlockLayers.registerLayer("goopyutil_demo_walltiles_top", 3, false);
        LayeredBlockLayers.registerLayer("goopyutil_demo_walltiles_bottom", 3, false);
        LayeredBlockLayers.registerLayer("goopyutil_demo_wall0", 0, true);
        LayeredBlockLayers.registerLayer("goopyutil_demo_wall1", 0, true);
    }
}
