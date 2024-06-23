package net.zephyr.goopyutil.client.gui.screens;

import net.minecraft.text.Text;

public abstract class EntityScreen extends GoopyScreen {

    int entityID = 0;
    public void putEntityID(int id){
        entityID = id;
    }
    public EntityScreen(Text title) {
        super(title);
    }
    public int getEntityID(){ return entityID; }
}
