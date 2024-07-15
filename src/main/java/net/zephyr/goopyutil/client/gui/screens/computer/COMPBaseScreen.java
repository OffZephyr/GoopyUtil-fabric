package net.zephyr.goopyutil.client.gui.screens.computer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.client.gui.screens.GoopyScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPBaseAppScreen;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.init.SoundsInit;
import net.zephyr.goopyutil.util.GoopyScreens;

import java.util.Objects;

public abstract class COMPBaseScreen extends GoopyScreen {
    public Identifier WALLPAPER = ComputerData.getWallpapers().get(0).getTexture();
    public COMPBaseScreen(Text title, NbtCompound nbt, long l) {
        super(title, nbt, l);
    }
    final int screenSizeBase = 256;
    public int screenSize = 256;

    private boolean doubleClicking = false;
    private int doubleClickingTime = 0;

    @Override
    protected void init() {
        changeWallpaper(getNbtData().getString("wallpaper"));

        super.init();
    }

    public void updateIndex(String currentScreen){
        if(GoopyScreens.getScreens().containsKey(currentScreen)) {
            this.getNbtData().putString("Window", currentScreen);
        }
        else {
            this.getNbtData().putString("Window", "default");
        }
    }

    @Override
    public void tick() {

        if(!(MinecraftClient.getInstance().world.getBlockState(getBlockPos()).isOf(BlockInit.COMPUTER))){
            close();
        }

        doubleClickingTime = doubleClickingTime > 0 ? doubleClickingTime - 1 : 0;
        doubleClicking = doubleClickingTime > 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        client.player.playSound(SoundsInit.CLICK_PRESS, 1, 1);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        client.player.playSound(SoundsInit.CLICK_RELEASE, 1, 1);

        if(!doubleClicking) {
            doubleClickingTime = 10;
            doubleClicking = true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void changeWallpaper(String name){
        getNbtData().putString("wallpaper", name);
        saveData();

        for(ComputerData.Initializer.Wallpaper wallpaper : ComputerData.getWallpapers()) {
            if(Objects.equals(wallpaper.getId(), getNbtData().getString("wallpaper"))){
                WALLPAPER = wallpaper.getTexture();
            }
        }
    }

    public boolean isDoubleClicking(){
        return doubleClicking;
    }

    public void saveData() {
        if(MinecraftClient.getInstance().currentScreen instanceof COMPBootupScreen) return;

        if (MinecraftClient.getInstance().currentScreen instanceof COMPBaseAppScreen appScreen) {
            this.updateIndex(appScreen.appName());
            GoopyScreens.saveNbtFromScreen(getNbtData(), getBlockPos());
            return;
        }

        this.updateIndex("default");
        GoopyScreens.saveNbtFromScreen(getNbtData(), getBlockPos());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.fill(0, 0, this.width, (this.height/2 - screenSize/2), 0xFF000000);
        context.fill(0, (this.height/2 - screenSize/2), (this.width/2 - screenSize/2), (this.height/2 + screenSize/2), 0xFF000000);
        context.fill((this.width/2 + screenSize/2), (this.height/2 - screenSize/2), this.width, (this.height/2 + screenSize/2), 0xFF000000);
        context.fill(0, (this.height/2 + screenSize/2), this.width, this.height, 0xFF000000);


    }

    @Override
    public void close() {
        saveData();
        super.close();
    }
}
