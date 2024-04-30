package net.zephyr.goopyutil.util.Computer;

import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.client.gui.screens.BlockEntityScreen;

public abstract class ComputerApp {
    String name;
    Identifier iconTexture;
    BlockEntityScreen screen;
    public ComputerApp(BlockEntityScreen screen, String name, Identifier iconTexture){
        this.screen = screen;
        this.name = name;
        this.iconTexture = iconTexture;
    }
    public String getName(){
        return this.name;
    }

    public BlockEntityScreen getScreen(){
        return this.screen;
    }
    public Identifier getIconTexture(){
        return this.iconTexture;
    }

    public void init(){

    }
    public void tickWhenOpen(){

    }
    public void tickAlways(){

    }
}
