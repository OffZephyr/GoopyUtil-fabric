package net.zephyr.goopyutil.client.gui.screens;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.blocks.camera.CameraBlock;
import net.zephyr.goopyutil.blocks.camera.CameraBlockEntity;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlock;
import net.zephyr.goopyutil.util.GoopyScreens;

import java.util.ArrayList;
import java.util.List;

public class CameraTabletScreen extends GoopyScreen {
    Identifier overlay = new Identifier(GoopyUtil.MOD_ID, "textures/gui/camera/camera_overlay.png");
    Identifier white = new Identifier(GoopyUtil.MOD_ID, "textures/block/white.png");
    long currentCam = 0;
    int curCamIndex = 0;
    List<Long> cams;
    boolean hideHud;
    int mapMultiplier = 1;
    int mapCornerPosX, mapCornerPosY, mapEndPosX, mapEndPosY, mapWidth, mapHeight;
    boolean holding = false;
    BlockPos minPos, maxPos;
    public CameraTabletScreen(Text title) {
        super(title);
    }
    @Override
    protected void init() {
        NbtCompound data = getNbtData();
        holding = false;
        mapCornerPosX = this.width - this.width/2;
        mapCornerPosY = this.height - (this.height/2 + this.height/12);
        mapEndPosX = this.width - this.width/18;
        mapEndPosY = this.height - this.height/8;
        minPos = BlockPos.fromLong(data.getLong("mapMinCorner"));
        maxPos = BlockPos.fromLong(data.getLong("mapMaxCorner"));

        this.hideHud = MinecraftClient.getInstance().options.hudHidden;
        MinecraftClient.getInstance().options.hudHidden = true;

        cams = new ArrayList<>();
        long[] camsData = getNbtData().getLongArray("Cameras");
        for (long cam : camsData) {
            cams.add(cam);
        }
        curCamIndex = getNbtData().getInt("currentCam");
        currentCam = !cams.isEmpty() && getNbtData().getInt("currentCam") < cams.size() ? cams.get(curCamIndex) : 0;

        super.init();
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(overlay, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
        drawMap(context, mouseX, mouseY);
        drawActionButton(context, mouseX, mouseY);

        super.render(context, mouseX, mouseY, delta);
    }

    void drawActionButton(DrawContext context, int mouseX, int mouseY){
        if(MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam)) != null) {
            NbtCompound nbt = ((GoopyBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam))).getCustomData().copy();
            if (nbt.getBoolean("Action")) {
                int width = textRenderer.getWidth(nbt.getString("ActionName") + 10);
                int height = 30;
                int diff = mapEndPosX + (mapMultiplier * 2) - (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier);
                int x = (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier) + (diff / 2) - width / 2;
                if (width - 10 > diff) x = mapEndPosX + (mapMultiplier * 2) + 5 - width;
                int y = (mapEndPosY - (mapHeight * mapMultiplier) - mapMultiplier) - 5 - height;
                context.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFFFFFFFF);
                boolean bl = isOnButton(mouseX, mouseY, x, y, width, height);
                int color = bl ? holding ? ColorHelper.Argb.getArgb(255, 75, 255, 75) : ColorHelper.Argb.getArgb(255, 150, 150, 150) : ColorHelper.Argb.getArgb(255, 100, 100, 100);
                context.fill(x, y, x + width, y + height, color);
                context.drawCenteredTextWithShadow(textRenderer, nbt.getString("ActionName"), x + width / 2, (y + (height / 2)) - 4, 0xFFFFFFFF);
            }
        }
    }
    void buttonCheck(double mouseX, double mouseY){
        if( MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam)) != null) {
            NbtCompound nbt = ((GoopyBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam))).getCustomData().copy();

            int width = textRenderer.getWidth(nbt.getString("ActionName") + 10);
            int height = 30;
            int diff = mapEndPosX + (mapMultiplier * 2) - (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier);
            int x = (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier) + (diff / 2) - width / 2;
            if (width - 10 > diff) x = mapEndPosX + (mapMultiplier * 2) + 5 - width;
            int y = (mapEndPosY - (mapHeight * mapMultiplier) - mapMultiplier) - 5 - height;
            boolean bl = isOnButton(mouseX, mouseY, x, y, width, height);

            setPowered(bl && holding);
        }
    }
    void drawMap(DrawContext context, int mouseX, int mouseY){
        NbtCompound data = getNbtData();

        boolean bl = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty();
        if(!bl) {

            int alpha = mouseX > mapCornerPosX && mouseX < mapEndPosX && mouseY > mapCornerPosY && mouseY < mapEndPosY ?
                    255 : 100;
            int color = ColorHelper.Argb.getArgb(alpha, 255, 255, 255);

            int mapMaxWidth = mapEndPosX - mapCornerPosX;
            int mapMaxHeight = mapEndPosY - mapCornerPosY;

            NbtList mapNbt = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();

            mapWidth = Math.abs(maxPos.getX() - minPos.getX());
            mapHeight = Math.abs(maxPos.getZ() - minPos.getZ());

            mapMultiplier = 1;
            while((mapWidth * (mapMultiplier+1) <= mapMaxWidth) && (mapHeight * (mapMultiplier+1) <= mapMaxHeight)){
                mapMultiplier++;
            }

            int bg1 = mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier;
            int bg2 = mapEndPosY - (mapHeight * mapMultiplier) - mapMultiplier;
            context.fill(bg1, bg2, mapEndPosX + (mapMultiplier*2), mapEndPosY + (mapMultiplier*2), 0x55000000);

            for(int i = 0; i < mapNbt.size(); i++) {
                if (mapNbt.get(i).getType() == NbtElement.LONG_ARRAY_TYPE) {
                    BlockPos pos1 = BlockPos.fromLong(mapNbt.getLongArray(i)[0]);
                    BlockPos pos2 = BlockPos.fromLong(mapNbt.getLongArray(i)[1]);

                    int x1 = (Math.min(pos1.getX(), pos2.getX())- minPos.getX()) * mapMultiplier;
                    int z1 = (Math.min(pos1.getZ(), pos2.getZ())- minPos.getZ()) * mapMultiplier;
                    int x2 = mapMultiplier + ((Math.max(pos1.getX(), pos2.getX())- minPos.getX()) * mapMultiplier);
                    int z2 = mapMultiplier + ((Math.max(pos1.getZ(), pos2.getZ())- minPos.getZ()) * mapMultiplier);

                    x1 += mapEndPosX - (mapWidth * mapMultiplier);
                    x2 += mapEndPosX - (mapWidth * mapMultiplier);
                    z1 += mapEndPosY - (mapHeight * mapMultiplier);
                    z2 += mapEndPosY - (mapHeight * mapMultiplier);

                    context.fill(x1, z1, x2, z2, color);
                }
            }

            for (Long cam : cams) {
                BlockPos pos = BlockPos.fromLong(cam);

                int x = (pos.getX() - minPos.getX()) * mapMultiplier;
                int z = (pos.getZ() - minPos.getZ()) * mapMultiplier;

                x += mapEndPosX - (mapWidth * mapMultiplier);
                z += mapEndPosY - (mapHeight * mapMultiplier);

                boolean bl2 = isOnButton(mouseX, mouseY, x, z, mapMultiplier, mapMultiplier);

                if (bl2 && MinecraftClient.getInstance().world != null) {
                    String name = ((CameraBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos)).getCustomData().getString("Name");
                    context.drawTooltip(MinecraftClient.getInstance().textRenderer, Text.literal(name), mouseX, mouseY);
                }
                int camColor = bl2 || cam == currentCam ? ColorHelper.Argb.getArgb(alpha, 75, 255, 75) : ColorHelper.Argb.getArgb(alpha, 100, 100, 100);
                context.fill(x, z, x + mapMultiplier, z + mapMultiplier, camColor);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.holding = true;
        buttonCheck(mouseX, mouseY);
        changeCam(mouseX, mouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        changeCam(mouseX, mouseY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.holding = false;
        buttonCheck(mouseX, mouseY);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    void changeCam(double mouseX, double mouseY) {
        for (Long cam : cams) {
            BlockPos pos = BlockPos.fromLong(cam);

            int x = (pos.getX() - minPos.getX()) * mapMultiplier;
            int z = (pos.getZ() - minPos.getZ()) * mapMultiplier;

            x += mapEndPosX - (mapWidth * mapMultiplier);
            z += mapEndPosY - (mapHeight * mapMultiplier);

            boolean bl2 = isOnButton(mouseX, mouseY, x, z, mapMultiplier, mapMultiplier) && cam != currentCam;

            if (bl2) {
                currentCam = cam;
                curCamIndex = cams.indexOf(currentCam);
                NbtCompound nbt = getNbtData();
                nbt.putInt("currentCam", curCamIndex);
                compileData(nbt);
            }
        }
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
    public void setPowered(boolean power){
        if(MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam)) != null) {
            NbtCompound nbt = ((GoopyBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam))).getCustomData().copy();
            if (nbt.getBoolean("Action")) {
                if (nbt.getBoolean("Powered") != power) {
                    nbt.putBoolean("Powered", power);
                    GoopyScreens.saveNbtFromScreen(nbt, BlockPos.fromLong(currentCam));
                }
            } else {
                if (nbt.getBoolean("Powered")) {
                    nbt.putBoolean("Powered", false);
                    GoopyScreens.saveNbtFromScreen(nbt, BlockPos.fromLong(currentCam));
                }
            }
        }
    }
    private void compileData(NbtCompound nbt){
        GoopyScreens.saveNbtFromScreen(nbt);
    }

    void setLight(long pos, boolean value){
        if(MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(pos)) != null) {
            NbtCompound nbt = ((GoopyBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(pos))).getCustomData().copy();
            if (nbt.getBoolean("Flashlight")) {
                if (nbt.getBoolean("Lit") != value) {
                    nbt.putBoolean("Lit", value);
                    GoopyScreens.saveNbtFromScreen(nbt, BlockPos.fromLong(pos));
                }
            } else {
                if (nbt.getBoolean("Lit")) {
                    nbt.putBoolean("Lit", false);
                    GoopyScreens.saveNbtFromScreen(nbt, BlockPos.fromLong(pos));
                }
            }
        }
    }
    void setUsed(long pos, boolean value){
        if(MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(pos)) != null) {
            NbtCompound nbt = ((GoopyBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(pos))).getCustomData().copy();
            if (nbt.getBoolean("isUsed") != value) {
                nbt.putBoolean("isUsed", value);
                GoopyScreens.saveNbtFromScreen(nbt, BlockPos.fromLong(pos));
            }
        }
    }

    @Override
    public void close() {
        for(long cam : cams){
            setUsed(cam, false);
            setLight(cam, false);
        }

        setAsUsed(false);
        MinecraftClient.getInstance().options.hudHidden = this.hideHud;
        super.close();
    }

    public Vec3d camPos(){
        if(MinecraftClient.getInstance().world != null) {
            BlockEntity entity = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
            if (entity != null) {
                BlockPos pos = BlockPos.fromLong(currentCam);
                Vec3d offset;
                float amount = 0.2f;
                switch (MinecraftClient.getInstance().world.getBlockState(pos).get(CameraBlock.FACING)) {
                    default -> offset = new Vec3d(0, 0, amount);
                    case SOUTH -> offset = new Vec3d(0, 0, -amount);
                    case EAST -> offset = new Vec3d(-amount, 0, 0);
                    case WEST -> offset = new Vec3d(amount, 0, 0);
                }

                return new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.7f, pos.getZ() + 0.5f).add(offset);
            }
        }
        return MinecraftClient.getInstance().cameraEntity != null ? MinecraftClient.getInstance().cameraEntity.getPos() : Vec3d.ZERO;
    }
    public float getPitch(){
        if(MinecraftClient.getInstance().world != null) {
            BlockEntity entity = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
            if (entity != null) {
                NbtCompound nbt = ((GoopyBlockEntity) entity).getCustomData().copy();
                return nbt.getFloat("pitch");
            }
        }
        return MinecraftClient.getInstance().cameraEntity != null ? MinecraftClient.getInstance().cameraEntity.getPitch() : 0;
}
    public float getYaw(){
        if(MinecraftClient.getInstance().world != null) {
            BlockEntity entity = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
            if (entity != null) {
                NbtCompound nbt = ((GoopyBlockEntity) entity).getCustomData().copy();
                BlockPos pos = BlockPos.fromLong(currentCam);
                World world = MinecraftClient.getInstance().world;
                return nbt.getFloat("yaw") + world.getBlockState(pos).get(LayeredBlock.FACING).asRotation();
            }
        }
        return MinecraftClient.getInstance().cameraEntity != null ? MinecraftClient.getInstance().cameraEntity.getYaw() : 0;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
