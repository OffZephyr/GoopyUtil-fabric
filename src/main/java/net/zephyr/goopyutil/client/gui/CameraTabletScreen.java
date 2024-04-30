package net.zephyr.goopyutil.client.gui;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.blocks.camera.CameraBlock;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlock;
import net.zephyr.goopyutil.client.gui.screens.GoopyScreen;
import net.zephyr.goopyutil.util.GoopyScreens;

import java.util.ArrayList;
import java.util.List;

public class CameraTabletScreen extends GoopyScreen {
    public CameraTabletScreen(Text title) {
        super(title);
    }
    long currentCam = 0;
    List<Long> cams;
    boolean hudHidden;

    @Override
    protected void init() {
        cams = new ArrayList<>();
        long[] camsData = getNbtData().getLongArray("Cameras");
        for (int i = 0; i < camsData.length; i++) {
            cams.add(camsData[i]);
        }
        currentCam = !cams.isEmpty() ? cams.get(getNbtData().getInt("currentCam")) : 0;

        MinecraftClient.getInstance().options.setPerspective(Perspective.FIRST_PERSON);
        this.hudHidden = MinecraftClient.getInstance().options.hudHidden;
        MinecraftClient.getInstance().options.hudHidden = true;

        super.init();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(MinecraftClient.getInstance().options.jumpKey.matchesKey(keyCode, scanCode)){
            setLight(currentCam, true);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if(MinecraftClient.getInstance().options.jumpKey.matchesKey(keyCode, scanCode)){
            setLight(currentCam, false);
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void tick() {
        for(long cam : cams){
            setUsed(cam, cam == currentCam);
        }
        super.tick();
    }

    public void setAsUsed(boolean used){
        NbtCompound nbt = getNbtData();
        nbt.putBoolean("used", used);
        compileData(nbt);
    }
    private void compileData(NbtCompound nbt){
        GoopyScreens.saveNbtFromScreen(nbt);
    }

    void setLight(long pos, boolean value){
        NbtCompound nbt = ((GoopyBlockEntity)MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(pos))).getCustomData().copy();
        if(nbt.getBoolean("Flashlight")) {
            if (nbt.getBoolean("Lit") != value) {
                nbt.putBoolean("Lit", value);
                GoopyScreens.saveNbtFromScreen(nbt, BlockPos.fromLong(pos));
            }
        }
        else {
            if (nbt.getBoolean("Lit")) {
                nbt.putBoolean("Lit", false);
                GoopyScreens.saveNbtFromScreen(nbt, BlockPos.fromLong(pos));
            }
        }
    }
    void setUsed(long pos, boolean value){
        NbtCompound nbt = ((GoopyBlockEntity)MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(pos))).getCustomData().copy();
        if(nbt.getBoolean("isUsed") != value) {
            nbt.putBoolean("isUsed", value);
            GoopyScreens.saveNbtFromScreen(nbt, BlockPos.fromLong(pos));
        }
    }

    @Override
    public void close() {
        for(long cam : cams){
            setUsed(cam, false);
            setLight(cam, false);
        }
        MinecraftClient.getInstance().setCameraEntity(MinecraftClient.getInstance().player);
        MinecraftClient.getInstance().options.hudHidden = this.hudHidden;

        setAsUsed(false);
        super.close();
    }

    public Vec3d camPos(){
        BlockEntity entity = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
        if(entity != null) {
            BlockPos pos = BlockPos.fromLong(currentCam);
            Vec3d offset;
            float amount = 0.2f;
            switch (MinecraftClient.getInstance().world.getBlockState(pos).get(CameraBlock.FACING)) {
                default -> offset = new Vec3d(0, 0, amount);
                case SOUTH -> offset = new Vec3d(0, 0, -amount);
                case EAST -> offset = new Vec3d(-amount, 0, 0);
                case WEST -> offset = new Vec3d(amount, 0, 0);
            }
            Vec3d vecPos = new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.7f, pos.getZ() + 0.5f).add(offset);

            return vecPos;
        }
        return Vec3d.ZERO;
    }
    public float getPitch(){
        BlockEntity entity = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
        if(entity != null) {
        NbtCompound nbt = ((GoopyBlockEntity)entity).getCustomData().copy();
        float pitch = nbt.getFloat("pitch");
        return pitch;
    }
        return 0;
}
    public float getYaw(){
        BlockEntity entity = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
        if(entity != null) {
            NbtCompound nbt = ((GoopyBlockEntity) entity).getCustomData().copy();
            BlockPos pos = BlockPos.fromLong(currentCam);
            World world = MinecraftClient.getInstance().world;
            float yaw = nbt.getFloat("yaw") + world.getBlockState(pos).get(LayeredBlock.FACING).asRotation();
            return yaw;
        }
        return 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
