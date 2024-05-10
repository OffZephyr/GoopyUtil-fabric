package net.zephyr.goopyutil.client.gui.screens.computer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.client.gui.screens.BlockEntityScreen;

public class COMPDesktopScreen extends COMPBaseScreen {
    public Identifier BOTTOM_BAR = new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/bottom_bar.png");
    public Identifier WALLPAPER_MENU = new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/wallpapers_window.png");
    public COMPDesktopScreen(Text title) {
        super(title);
    }
    boolean holding = false;
    boolean dragging = false;
    boolean wallpaperSelect = false;

    int gotMouseX = 0, gotMouseY = 0;

    float wallpapersListOffset = 0;
    float wallpapersListOffsetDiff = 0;
    float wallpapersListOffsetOld = 0;
    int iconGrid = 4;

    @Override
    protected void init() {
        this.holding = false;
        dragging = false;
        this.wallpaperSelect = false;
        this.BOTTOM_BAR = new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/bottom_bar.png");
        iconGrid = 4;

        saveData();
        super.init();
    }

    @Override
    public void tick() {
        super.tick();
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        float bottomBarHeight = (13 / 256f) * screenSize;
        float wallpaperButtonLeft = this.width / 2f + this.screenSize / 2f - ((64 / 256f) * screenSize);
        float wallpaperButtonRight = this.width / 2f + this.screenSize / 2f;
        boolean onWallpaperButton = mouseX > wallpaperButtonLeft && mouseX < wallpaperButtonRight && mouseY > (this.height / 2 + this.screenSize / 2) - (int) bottomBarHeight && mouseY < (this.height / 2 + this.screenSize / 2);

        if(onWallpaperButton) wallpaperSelect = !wallpaperSelect;

        if(!dragging){
            if(wallpaperSelect) {
                float wallpaperMenuHeight = (96 / 256f) * screenSize;
                float wallpaperSize = wallpaperMenuHeight / 2;
                float wallpaperOffset = ((20f / 256f) * screenSize);
                float wallpaperHeight = ((20 / 256f) * screenSize) + ((63 / 256f) * screenSize) / 2 - wallpaperSize / 2;

                for (int i = 0; i < ComputerData.getWallpapers().size(); i++) {
                    int x = this.width / 2 - this.screenSize / 2 + (int) wallpaperOffset + (i * ((int) wallpaperSize + (int) wallpaperOffset)) + (int) wallpapersListOffset;
                    int y = (this.height / 2 + this.screenSize / 2) - (int) wallpaperMenuHeight + (int) wallpaperHeight;

                    if (mouseX > x && mouseY > y && mouseX < x + wallpaperSize && mouseY < y + wallpaperSize && !dragging) {
                        changeWallpaper(ComputerData.getWallpapers().get(i).getId());
                    }
                }
            }
        }

        holding = false;
        dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        holding = true;
        float wallpaperMenuHeight = (96 / 256f) * screenSize;
        if(wallpaperSelect && mouseY < (this.height / 2 + this.screenSize / 2) - (int) wallpaperMenuHeight) wallpaperSelect = false;


        for(int i = 0; i < ComputerData.getApps().size(); i++){
            final int multiplier = i/iconGrid;

            float iconGridSize = screenSize - (2 * ((13 / 256f) * screenSize));

            int x = (int) (((width/2)-(iconGridSize/2) + (i * (iconGridSize / iconGrid))) - (multiplier * iconGridSize));
            int y = (int) ((height/2)-(iconGridSize/2) + (multiplier * (iconGridSize/iconGrid)));

            if(mouseX > x && mouseY > y && mouseX < x + (int) iconGridSize/iconGrid && mouseY < y + (int) iconGridSize/iconGrid){
                if(isDoubleClicking()) {
                    BlockEntityScreen screen = ComputerData.getApps().get(i).getScreen();

                    screen.putNbtData(this.getNbtData());
                    screen.putBlockPos(this.getBlockPos());
                    MinecraftClient.getInstance().setScreen(screen);
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(wallpaperSelect) {
            this.wallpapersListOffset -= (amount*15);
            this.wallpapersListOffsetOld -= (amount*15);
        }
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

        context.drawTexture(WALLPAPER, (width/2)-(screenSize/2), (height/2)-(screenSize/2), 0, 0, screenSize, screenSize, screenSize, screenSize);

        for(int i = 0; i < ComputerData.getApps().size(); i++){
            Identifier appIcon = ComputerData.getApps().get(i).getIconTexture();

            while(ComputerData.getApps().size() > iconGrid * iconGrid){
                iconGrid++;
            }

            final int multiplier = i/iconGrid;

             float iconGridSize = screenSize - (2 * ((13 / 256f) * screenSize));

            int x = (int) (((width/2)-(iconGridSize/2) + (i * (iconGridSize / iconGrid))) - (multiplier * iconGridSize));
            int y = (int) ((height/2)-(iconGridSize/2) + (multiplier * (iconGridSize/iconGrid)));
            context.drawTexture(appIcon, x, y, 0, 0, (int) iconGridSize/iconGrid, (int) iconGridSize/iconGrid, (int) iconGridSize/iconGrid, (int) iconGridSize/iconGrid);
        }

        if(wallpaperSelect) {
            float wallpaperMenuHeight = (96 / 256f) * screenSize;
            context.fill(this.width / 2 - this.screenSize / 2, (this.height / 2 + this.screenSize / 2) - (int) wallpaperMenuHeight, this.width / 2 + this.screenSize / 2, (this.height / 2 + this.screenSize / 2), 0xFF182562);

            float wallpaperSize = wallpaperMenuHeight/2;

            for (int i = 0; i < ComputerData.getWallpapers().size(); i++) {
                float wallpaperHeight = ((20 / 256f) * screenSize) + ((63 / 256f) * screenSize) / 2 - wallpaperSize / 2;
                Identifier wallpaper = ComputerData.getWallpapers().get(i).getTexture();

                float wallpaperOffset = ((20f / 256f) * screenSize);
                if(dragging){
                    wallpapersListOffset = wallpapersListOffsetOld + wallpapersListOffsetDiff;
                    wallpapersListOffsetDiff = mouseX - gotMouseX;
                }
                else {
                    wallpapersListOffsetOld = wallpapersListOffset;
                    wallpapersListOffsetDiff = 0;

                    if (wallpapersListOffset > 0) {
                        wallpapersListOffset += (0 - wallpapersListOffset) / 30;
                    }
                    if (wallpapersListOffset < (-(ComputerData.getWallpapers().size() * (wallpaperSize + wallpaperOffset)) + 175)) {
                        wallpapersListOffset += ((-(ComputerData.getWallpapers().size() * (wallpaperSize + wallpaperOffset)) + 175) - wallpapersListOffset) / 30;
                    }
                }
                int x = this.width / 2 - this.screenSize / 2 + (int) wallpaperOffset + (i * ((int) wallpaperSize + (int) wallpaperOffset)) + (int) wallpapersListOffset;
                int y =(this.height / 2 + this.screenSize / 2) - (int) wallpaperMenuHeight + (int) wallpaperHeight;

                context.drawTexture(wallpaper, x, y, 0, 0, (int) wallpaperSize, (int) wallpaperSize, (int) wallpaperSize, (int) wallpaperSize);

                if(mouseX > x && mouseY > y && mouseX < x + wallpaperSize && mouseY < y + wallpaperSize && !dragging){
                    if(holding)
                        context.fill(x, y, x + (int) wallpaperSize, y + (int) wallpaperSize, 0x66FFFFFF);
                    else
                        context.fill(x, y, x + (int) wallpaperSize, y + (int) wallpaperSize, 0x33FFFFFF);
                }

            }
            context.drawTexture(WALLPAPER_MENU, this.width / 2 - this.screenSize / 2, (this.height / 2 + this.screenSize / 2) - (int) wallpaperMenuHeight, 0, 0, screenSize, (int) wallpaperMenuHeight, screenSize, (int) wallpaperMenuHeight);
        }
        else {
            for(int i = 0; i < ComputerData.getApps().size(); i++){
                final int multiplier = i/iconGrid;

                float iconGridSize = screenSize - (2 * ((13 / 256f) * screenSize));

                int x = (int) (((width/2)-(iconGridSize/2) + (i * (iconGridSize / iconGrid))) - (multiplier * iconGridSize));
                int y = (int) ((height/2)-(iconGridSize/2) + (multiplier * (iconGridSize/iconGrid)));

                if(mouseX > x && mouseY > y && mouseX < x + (int) iconGridSize/iconGrid && mouseY < y + (int) iconGridSize/iconGrid)
                    context.fill(x, y, x + (int) iconGridSize/iconGrid, y + (int) iconGridSize/iconGrid, 0x66FFFFFF);
            }
        }

        float bottomBarHeight = (13 / 256f) * screenSize;
        float bottomBarTextureHeight = (52 / 256f) * screenSize;

        float wallpaperButtonLeft = this.width / 2f + this.screenSize / 2f - ((64 / 256f) * screenSize);
        float wallpaperButtonRight = this.width / 2f + this.screenSize / 2f;
        boolean onWallpaperButton = mouseX > wallpaperButtonLeft && mouseX < wallpaperButtonRight && mouseY > (this.height / 2 + this.screenSize / 2) - (int) bottomBarHeight && mouseY < (this.height / 2 + this.screenSize / 2);

        if (onWallpaperButton) {
            if (holding) {
                context.drawTexture(BOTTOM_BAR, this.width / 2 - this.screenSize / 2, (this.height / 2 + this.screenSize / 2) - (int) bottomBarHeight, 0, (26 / 256f) * screenSize, screenSize, (int) bottomBarHeight, screenSize, (int) bottomBarTextureHeight);
            } else {
                context.drawTexture(BOTTOM_BAR, this.width / 2 - this.screenSize / 2, (this.height / 2 + this.screenSize / 2) - (int) bottomBarHeight, 0, (13 / 256f) * screenSize, screenSize, (int) bottomBarHeight, screenSize, (int) bottomBarTextureHeight);
            }
        } else {
            context.drawTexture(BOTTOM_BAR, this.width / 2 - this.screenSize / 2, (this.height / 2 + this.screenSize / 2) - (int) bottomBarHeight, 0, 0, screenSize, (int) bottomBarHeight, screenSize, (int) bottomBarTextureHeight);
        }

        super.render(context, mouseX, mouseY, delta);
    }
}