package net.zephyr.goopyutil.client.gui.screens.killscreens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.goopyutil.util.mixinAccessing.IGetClientManagers;

public class DefaultGoopyUtilKillScreen extends AbstractGoopyUtilKillScreen {

    public DefaultGoopyUtilKillScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }

    @Override
    SoundEvent jumpscareSound() {
        return null;
    }

    @Override
    public void renderDeathImage(DrawContext context, int mouseX, int mouseY, float delta) {

    }

}
