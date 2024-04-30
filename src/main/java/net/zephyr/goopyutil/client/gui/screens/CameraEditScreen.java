package net.zephyr.goopyutil.client.gui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.util.GoopyScreens;

public class CameraEditScreen extends BlockEntityScreen {
    Identifier texture = new Identifier(GoopyUtil.MOD_ID, "textures/gui/camera/camera_edit.png");
    boolean isActive = false;
    private TextFieldWidget nameField;
    boolean holding = false;
    byte modeX = 0;
    byte modeY = 0;

    byte speedX = 0, speedY = 0;

    final short sliderSizeMin = -44;
    final short sliderSizeMax = 43;
    double xSlider = 0, ySlider = 0,
            xRange0 = 0, yRange0 = 0,
            xRange1 = 0, yRange1 = 0;

    boolean holdingXSlider = false, holdingYSlider = false,
            holdingXRangeMin = false, holdingXRangeMax = false,
            holdingYRangeMin = false, holdingYRangeMax = false;

    boolean flashlight = false;
    boolean action = false;
    byte nightvision = 0;

    public CameraEditScreen(Text title) {
        super(title);
    }
    @Override
    protected void init() {
        this.isActive = getNbtData().getBoolean("Active");
        int i = this.width / 2 - 43;
        int j =  this.height / 2 - 25;
        this.nameField = new TextFieldWidget(this.textRenderer, i, j, 82, 12, Text.translatable("container.repair"));
        this.nameField.setFocusUnlocked(false);
        this.nameField.setEditableColor(-1);
        this.nameField.setUneditableColor(-1);
        this.nameField.setDrawsBackground(false);
        this.nameField.setMaxLength(50);
        this.nameField.setChangedListener(this::onRenamed);
        this.nameField.setText(getNbtData().getString("Name"));
        this.addSelectableChild(this.nameField);
        this.setInitialFocus(this.nameField);
        this.nameField.setEditable(true);

        this.modeX = getNbtData().getByte("ModeX");
        this.modeY = getNbtData().getByte("ModeY");
        this.xSlider = -getNbtData().getDouble("yaw");
        this.ySlider = -getNbtData().getDouble("pitch");
        this.xRange0 = getNbtData().getDouble("minYaw");
        this.xRange1 = getNbtData().getDouble("maxYaw");
        this.yRange0 = getNbtData().getDouble("minPitch");
        this.yRange1 = getNbtData().getDouble("maxPitch");
        this.speedX = getNbtData().getByte("yawSpeed");
        this.speedY = getNbtData().getByte("pitchSpeed");

        this.flashlight = getNbtData().getBoolean("Flashlight");
        this.action = getNbtData().getBoolean("Action");
        this.nightvision = getNbtData().getByte("NightVision");

        this.holding = false;
        super.init();
    }

    private void onRenamed(String name){
        NbtCompound newData = getNbtData().copy();
        newData.putString("Name", name);
        compileData(newData);
    }

    private void compileData(){
        NbtCompound newData = getNbtData().copy();
        compileData(newData);
    }
    private void compileData(NbtCompound nbt){
        nbt.putBoolean("Active", this.isActive);
        nbt.putByte("ModeX", this.modeX);
        nbt.putByte("ModeY", this.modeY);
        nbt.putDouble("yaw", -this.xSlider);
        nbt.putDouble("pitch", -this.ySlider);
        nbt.putDouble("minYaw", this.xRange0);
        nbt.putDouble("maxYaw", this.xRange1);
        nbt.putDouble("minPitch", this.yRange0);
        nbt.putDouble("maxPitch", this.yRange1);
        nbt.putByte("yawSpeed", this.speedX);
        nbt.putByte("pitchSpeed", this.speedY);
        nbt.putBoolean("Flashlight", this.flashlight);
        nbt.putBoolean("Action", this.action);
        nbt.putByte("NightVision", this.nightvision);

        GoopyScreens.saveNbtFromScreen(nbt, getBlockPos());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.holding = true;

        if (mouseX > this.width / 2f - 77 && mouseX < this.width / 2f - 66 && mouseY > this.height / 2f + 6 && mouseY < this.height / 2f + 20) {
            this.isActive = !this.isActive;
            float pitch = this.isActive ? 1f : 0.85f;
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, pitch);
            compileData();
            return super.mouseClicked(mouseX, mouseY, button);
        }


        if(isOnButton(mouseX, mouseY, this.width/2 + 51, this.height / 2 + 9, 7, 7)){
            if(button == 0){
                speedX++;
                compileData();
            }
            else if(button == 1){
                speedX--;
                compileData();
            }
        }
        else if(isOnButton(mouseX, mouseY, this.width/2 + 51, this.height / 2 + 21, 7, 7)){
            if(button == 0){
                speedY++;
                compileData();
            }
            else if(button == 1){
                speedY--;
                compileData();
            }
        }

        if(isActive) {
            for (byte i = 0; i < 3; i++) {
                int xOffset = i * 13;
                if (isOnButton(mouseX, mouseY, this.width / 2 - 47 + xOffset, this.height / 2 - 8, 11, 11)) {
                    if(this.modeX != i){
                        MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                        this.modeX = i;
                        compileData();
                    }
                }
            }
            for (byte i = 0; i < 3; i++) {
                int xOffset = i * 13;
                if (isOnButton(mouseX, mouseY, this.width / 2 + 10 + xOffset, this.height / 2 - 8, 11, 11)) {
                    if(this.modeY != i) {
                        MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                        this.modeY = i;
                        compileData();
                    }
                }

            }

            if(modeX == 0){
                int x = this.width / 2 - 2 + (int)xSlider;
                if(isOnButton(mouseX, mouseY, x, this.height / 2 + 9, 5, 7)){
                    this.holdingXSlider = true;
                }
            }
            else {
                int x = this.width / 2 - 2;
                int x1 = x + (int)xRange0;
                int x2 = x + (int)xRange1;
                if(isOnButton(mouseX, mouseY, x1, this.height / 2 + 9, 5, 7)){
                    this.holdingXRangeMin = true;
                }
                else if(isOnButton(mouseX, mouseY, x2, this.height / 2 + 9, 5, 7)){
                    this.holdingXRangeMax = true;
                }
            }
            if(modeY == 0){
                int x = this.width / 2 - 2 + (int)ySlider;
                if(isOnButton(mouseX, mouseY, x, this.height / 2 + 21, 5, 7)){
                    this.holdingYSlider = true;
                }
            }
            else {
                int x = this.width / 2 - 2;
                int x1 = x + (int)yRange0;
                int x2 = x + (int)yRange1;
                if(isOnButton(mouseX, mouseY, x1, this.height / 2 + 21, 5, 7)){
                    this.holdingYRangeMin = true;
                }
                else if(isOnButton(mouseX, mouseY, x2, this.height / 2 + 21, 5, 7)){
                    this.holdingYRangeMax = true;
                }
            }
            if(isOnButton(mouseX, mouseY, this.width / 2 - 77, this.height / 2 - 28, 10, 8)){
                MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                this.flashlight = !flashlight;
                compileData();
            }
            if(isOnButton(mouseX, mouseY, this.width / 2 - 77, this.height / 2 - 19, 10, 8)){
                MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                this.action = !action;
                compileData();
            }
            if(isOnButton(mouseX, mouseY, this.width / 2 - 77, this.height / 2 - 10, 10, 8)){
                MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                this.nightvision = nightvision - 1 < 0 ? 2 : (byte)(nightvision - 1);
                compileData();
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(this.holdingXSlider){
            double comp = this.xSlider + deltaX;
            this.xSlider = comp < sliderSizeMin ? sliderSizeMin : comp > sliderSizeMax ? sliderSizeMax : comp;
        }
        else if(this.holdingYSlider){
            double comp = this.ySlider + deltaX;
            this.ySlider = comp < sliderSizeMin ? sliderSizeMin : comp > sliderSizeMax ? sliderSizeMax : comp;
        }
        else if(this.holdingXRangeMin){
            double comp = this.xRange0 + deltaX;
            this.xRange0 = comp < sliderSizeMin ? sliderSizeMin : Math.min(comp, xRange1 - 5);
        }
        else if(this.holdingXRangeMax){
            double comp = this.xRange1 + deltaX;
            this.xRange1 = comp < xRange0 + 5 ? xRange0 + 5 : comp > sliderSizeMax ? sliderSizeMax : comp;
        }
        else if(this.holdingYRangeMin){
            double comp = this.yRange0 + deltaX;
            this.yRange0 = comp < sliderSizeMin ? sliderSizeMin : Math.min(comp, yRange1 - 5);
        }
        else if(this.holdingYRangeMax){
            double comp = this.yRange1 + deltaX;
            this.yRange1 = comp < yRange0 + 5 ? yRange0 + 5 : comp > sliderSizeMax ? sliderSizeMax : comp;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.holding = false;
        if (this.holdingXSlider ||
                this.holdingYSlider ||
                this.holdingXRangeMin ||
                this.holdingXRangeMax ||
                this.holdingYRangeMin ||
                this.holdingYRangeMax) {

            this.holdingXSlider = false;
            this.holdingYSlider = false;
            this.holdingXRangeMin = false;
            this.holdingXRangeMax = false;
            this.holdingYRangeMin = false;
            this.holdingYRangeMax = false;
            compileData();
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(isOnButton(mouseX, mouseY, this.width/2 + 51, this.height / 2 + 9, 7, 7)){
            if(amount > 0) {
                speedX = speedX + 1 > 8 ? 8 : (byte)(speedX + 1);
            }
            else {
                speedX = speedX - 1 < 0 ? 0 : (byte)(speedX - 1);
            }
            compileData();
        }
        else if(isOnButton(mouseX, mouseY, this.width/2 + 51, this.height / 2 + 21, 7, 7)){
            if(amount > 0) {
                speedY = speedY + 1 > 8 ? 8 : (byte)(speedY + 1);
            }
            else {
                speedY = speedY - 1 < 0 ? 0 : (byte)(speedY - 1);
            }
            compileData();
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        renderBackground(context);

        context.drawTexture(texture, this.width/2 - 81, this.height/2 - 36, 0, 0, 146, 72, 256, 256);

        if(this.isActive){
            if (mouseX > this.width / 2 - 77 && mouseX < this.width / 2 - 66 && mouseY > this.height / 2 + 6 && mouseY < this.height / 2 + 20) {
                context.drawTexture(texture, this.width / 2 - 77, this.height / 2 + 6, 77, 72, 11, 15, 256, 256);
            }
            else {
                context.drawTexture(texture, this.width / 2 - 77, this.height / 2 + 6, 66, 72, 11, 15, 256, 256);
            }

            if(flashlight){
                renderButton(context, this.width / 2 - 77, this.height / 2 - 28, 98, 72, 98, 80, 10, 8, 256, 256, mouseX, mouseY);
            }

            if(action){
                renderButton(context, this.width / 2 - 77, this.height / 2 - 19, 108, 72, 108, 80, 10, 8, 256, 256, mouseX, mouseY);
            }

            if(nightvision == 1){
                renderButton(context, this.width / 2 - 77, this.height / 2 - 10, 118, 72, 118, 80, 10, 8, 256, 256, mouseX, mouseY);
            }
            else if(nightvision == 2){
                renderButton(context, this.width / 2 - 77, this.height / 2 - 10, 128, 72, 128, 80, 10, 8, 256, 256, mouseX, mouseY);
            }

            for(int i = 0; i < 3; i++)
            {
                int u = i*11;
                int v = modeX == i ? 72 : 94;
                int xOffset = i*13;
                    renderButton(context, this.width / 2 - 47 + xOffset, this.height / 2 - 8, u, v, u, 83, 11, 11, 256, 256, mouseX, mouseY);
            }
            for(int i = 0; i < 3; i++)
            {
                int u = i > 0 ? 22 + i*11 : 0;
                int v = modeY == i ? 72 : 94;
                int xOffset = i*13;
                renderButton(context, this.width / 2 + 10 + xOffset, this.height / 2 - 8, u, v, u, 83, 11, 11, 256, 256, mouseX, mouseY);
            }

            if(modeX == 0){
                int x = this.width / 2 - 2 + (int)xSlider;
                renderButton(context, x, this.height / 2 + 9, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);
            }
            else {
                int x = this.width / 2 - 2;
                int x1 = x + (int)xRange0;
                int x2 = x + (int)xRange1;
                renderButton(context, x1, this.height / 2 + 9, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);
                renderButton(context, x2, this.height / 2 + 9, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);

                context.fill(x - 44, this.height / 2 + 9, x1, this.height / 2 + 16, 0xFF555555);
                context.fill(x2 + 5, this.height / 2 + 9, x + 48, this.height / 2 + 16, 0xFF555555);
            }

            if(modeY == 0){
                int x = this.width / 2 - 2 + (int)ySlider;
                renderButton(context, x, this.height / 2 + 21, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);
            }
            else {
                int x = this.width / 2 - 2;
                int x1 = x + (int)yRange0;
                int x2 = x + (int)yRange1;
                renderButton(context, x1, this.height / 2 + 21, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);
                renderButton(context, x2, this.height / 2 + 21, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);

                context.fill(x - 44, this.height / 2 + 21, x1, this.height / 2 + 28, 0xFF555555);
                context.fill(x2 + 5, this.height / 2 + 21, x + 48, this.height / 2 + 28, 0xFF555555);
            }

            //context.drawTexture(texture, this.width/2 - 47, this.height/2 - 8, 0, 72, 11, 11, 256, 256);

        }
        else {
            if (mouseX > this.width / 2 - 77 && mouseX < this.width / 2 - 66 && mouseY > this.height / 2 + 6 && mouseY < this.height / 2 + 20) {
                context.drawTexture(texture, this.width / 2 - 77, this.height / 2 + 6, 55, 72, 11, 15, 256, 256);
            }
        }

        context.drawTexture(texture, this.width / 2 - 46, this.height / 2 - 29, 154, 0, 94, 16, 256, 256);

        context.drawTexture(texture, this.width/2 + 51, this.height / 2 + 9, 146, speedX * 7, 7, 7, 256, 256);
        context.drawTexture(texture, this.width/2 + 51, this.height / 2 + 21, 146, speedY * 7, 7, 7, 256, 256);

        this.nameField.render(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
    }

    boolean isOnButton(double mouseX, double mouseY, int x, int y, int width, int height) {
        return (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height);
    }

    void renderButton(DrawContext context, int x, int y, int u, int v, int u2, int v2, int width, int height, int textureWidth, int textureHeight, int mouseX, int mouseY){
        if(isOnButton(mouseX, mouseY, x, y, width, height)){
            context.drawTexture(texture, x, y, u2, v2, width, height, textureWidth, textureHeight);
        }
        else {
            context.drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
