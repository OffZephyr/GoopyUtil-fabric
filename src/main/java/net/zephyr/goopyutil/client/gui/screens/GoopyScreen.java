package net.zephyr.goopyutil.client.gui.screens;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.types.Type;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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

    public boolean isOnButton(double mouseX, double mouseY, int x, int y, int width, int height) {
        return (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height);
    }

    public void renderButton(Identifier texture, DrawContext context, int x, int y, int u, int v, int u2, int v2, int width, int height, int textureWidth, int textureHeight, int mouseX, int mouseY){
        if(isOnButton(mouseX, mouseY, x, y, width, height)){
            context.drawTexture(texture, x, y, u2, v2, width, height, textureWidth, textureHeight);
        }
        else {
            context.drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }
}
