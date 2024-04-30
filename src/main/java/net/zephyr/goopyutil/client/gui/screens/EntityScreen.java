package net.zephyr.goopyutil.client.gui.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.networking.NetChannels;

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
