package net.zephyr.goopyutil.client.gui.screens;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.types.Type;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.util.GoopyScreens;

import java.util.Set;

public abstract class GoopyScreen extends Screen {
    NbtCompound nbtData = new NbtCompound();
    public void putNbtData(NbtCompound nbt){
        nbtData = nbt.copy();
    }
    public NbtCompound getNbtData(){
        return nbtData;
    }
    public GoopyScreen(Text title) {
        super(title);
    }
}
