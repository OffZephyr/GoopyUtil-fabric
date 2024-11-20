package net.zephyr.goopyutil.client.gui.screens.killscreens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.init.SoundsInit;
import net.zephyr.goopyutil.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.goopyutil.util.mixinAccessing.IGetClientManagers;

public class ZephyrKillScreen extends AbstractGoopyUtilKillScreen {
    public ZephyrKillScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }

    @Override
    SoundEvent jumpscareSound() {
        return SoundsInit.ZEPHYR_JUMPSCARE;
    }

    @Override
    public void renderDeathImage(DrawContext context, int mouseX, int mouseY, float delta) {
        EntityDataManager manager = ((IGetClientManagers) MinecraftClient.getInstance()).getEntityDataManager();

        String textureName = manager.getSkin(type, skin) == null || skin.isEmpty() ? "default" : skin;
        String textureTrimmed = textureName.replace("entity.goopyutil.zephyr.", "");
        Identifier texture = Identifier.of(GoopyUtil.MOD_ID, "textures/gui/killscreens/zephyr/" + textureTrimmed + ".png");

        drawRecolorableTexture(context, texture, 0, 0, 0, this.width, this.height, 0, 0, this.width, this.height, 1, 1, 1, 1);
    }
}
