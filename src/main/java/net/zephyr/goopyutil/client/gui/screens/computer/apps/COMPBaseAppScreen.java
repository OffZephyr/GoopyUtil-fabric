package net.zephyr.goopyutil.client.gui.screens.computer.apps;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.client.gui.screens.BlockEntityScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPBaseScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPDesktopScreen;

public abstract class COMPBaseAppScreen extends COMPBaseScreen {
    public Identifier BOTTOM_BAR = new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/bottom_bar.png");
    public Identifier WINDOW_BASE = new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/window_base.png");
    public Identifier BUTTONS = new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/computer_buttons.png");
    public COMPBaseAppScreen(Text title) {
        super(title);
    }
    boolean holding = false;
    boolean dragging = false;

    float topCornerX;
    float topCornerY;
    float appAvailableSizeX;
    float appAvailableSizeY;

    int gotMouseX = 0, gotMouseY = 0;

    @Override
    protected void init() {
        this.holding = false;
        dragging = false;

        saveData();
        super.init();
    }

    public boolean getHolding(){
        return holding;
    }
    public boolean getDragging(){
        return dragging;
    }
    public int mouseXBeforeDragging(){
        return gotMouseX;
    }
    public int mouseYBeforeDragging(){
        return gotMouseY;
    }

    @Override
    public void tick() {
        appAvailableSizeX = (240 / 256f) * screenSize;
        appAvailableSizeY = (224 / 256f) * screenSize;
        topCornerX = this.width/2f - appAvailableSizeX/2f;
        topCornerY = this.height/2f - appAvailableSizeY/2f + ((8 / 256f) * screenSize);
        super.tick();
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        holding = false;
        dragging = false;


        float appWindowSize = (248 / 256f) * screenSize;
        float buttonSize = (16 / 256f) * screenSize;
        float buttonOffset = (2f / 256f) * screenSize;
        if(mouseX > this.width / 2 + (int)appWindowSize / 2 - (int)buttonOffset - (int) buttonSize && mouseX < this.width / 2 + (int)appWindowSize / 2 - (int)buttonOffset && mouseY > this.height / 2 - (int)appWindowSize / 2 + (int)buttonOffset && mouseY < this.height / 2 - (int)appWindowSize / 2 + (int)buttonOffset + (int) buttonSize) {
            BlockEntityScreen screen = new COMPDesktopScreen(title);
            screen.putNbtData(this.getNbtData());
            screen.putBlockPos(this.getBlockPos());
            MinecraftClient.getInstance().setScreen(screen);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        holding = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        dragging = true;
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(!dragging) {gotMouseX = mouseX; gotMouseY = mouseY;}

        float borderLineSize = (8 / 256f) * screenSize;
        float bottomBarHeight = (13 / 256f) * screenSize;
        float bottomBarTextureHeight = (52 / 256f) * screenSize;
        float appWindowSize = (248 / 256f) * screenSize;

        context.drawTexture(WALLPAPER, (width/2)-(screenSize/2), (height/2)-(screenSize/2), 0, 0, screenSize, (int)borderLineSize, screenSize, screenSize);
        context.drawTexture(WALLPAPER, (width/2)-(screenSize/2), (height/2)-(screenSize/2) + (int)borderLineSize, 0, borderLineSize, (int)borderLineSize, screenSize - (2*(int)borderLineSize), screenSize, screenSize);
        context.drawTexture(WALLPAPER, (width/2)+(screenSize/2) - (int)borderLineSize, (height/2)-(screenSize/2) + (int)borderLineSize, appWindowSize, borderLineSize, (int)borderLineSize, screenSize - (2*(int)borderLineSize), screenSize, screenSize);
        context.drawTexture(WALLPAPER, (width/2)-(screenSize/2), (height/2)+(screenSize/2) - (int)borderLineSize, 0, appWindowSize, screenSize, (int)borderLineSize, screenSize, screenSize);


        context.drawTexture(BOTTOM_BAR, this.width / 2 - this.screenSize / 2, (this.height / 2 + this.screenSize / 2) - (int) bottomBarHeight, 0, (39 / 256f) * screenSize, screenSize, (int) bottomBarHeight, screenSize, (int) bottomBarTextureHeight);


        context.drawTexture(WINDOW_BASE, this.width / 2 - (int)appWindowSize / 2, this.height / 2 - (int)appWindowSize / 2, 0, 0, (int)appWindowSize, (int)appWindowSize, (int)appWindowSize, (int)appWindowSize);

        float buttonTextureSize = (128 / 256f) * screenSize;
        float buttonSize = (16 / 256f) * screenSize;
        float buttonOffset = (2f / 256f) * screenSize;
        if(mouseX > this.width / 2 + (int)appWindowSize / 2 - (int)buttonOffset - (int) buttonSize && mouseX < this.width / 2 + (int)appWindowSize / 2 - (int)buttonOffset && mouseY > this.height / 2 - (int)appWindowSize / 2 + (int)buttonOffset && mouseY < this.height / 2 - (int)appWindowSize / 2 + (int)buttonOffset + (int) buttonSize){
            if(holding){
                context.drawTexture(BUTTONS, this.width / 2 + (int)appWindowSize / 2 - (int)buttonOffset - (int) buttonSize, this.height / 2 - (int)appWindowSize / 2 + (int)buttonOffset, buttonSize*2, 0, (int)buttonSize, (int)buttonSize, (int)buttonTextureSize, (int)buttonTextureSize);
            }
            else {
                context.drawTexture(BUTTONS, this.width / 2 + (int)appWindowSize / 2 - (int)buttonOffset - (int) buttonSize, this.height / 2 - (int)appWindowSize / 2 + (int)buttonOffset, buttonSize, 0, (int)buttonSize, (int)buttonSize, (int)buttonTextureSize, (int)buttonTextureSize);
            }
        }
        else {
            context.drawTexture(BUTTONS, this.width / 2 + (int)appWindowSize / 2 - (int)buttonOffset - (int) buttonSize, this.height / 2 - (int)appWindowSize / 2 + (int)buttonOffset, 0, 0, (int)buttonSize, (int)buttonSize, (int)buttonTextureSize, (int)buttonTextureSize);
        }

        super.render(context, mouseX, mouseY, delta);
    }
}