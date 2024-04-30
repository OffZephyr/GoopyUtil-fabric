package net.zephyr.goopyutil.blocks.computer.Apps;

import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.client.gui.screens.BlockEntityScreen;
import net.zephyr.goopyutil.util.Computer.ComputerApp;

public class MusicApp extends ComputerApp {
    public MusicApp(BlockEntityScreen screen, String name, Identifier iconTexture) {
        super(screen, name, iconTexture);
    }

    @Override
    public void init() {
    }

    @Override
    public void tickWhenOpen() {
    }
    @Override
    public void tickAlways() {
    }
}
