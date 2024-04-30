package net.zephyr.goopyutil.client.gui.screens.computer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.blocks.computer.ComputerBlockEntity;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.util.Computer.ComputerApp;
import net.zephyr.goopyutil.util.GoopyScreens;
import net.zephyr.goopyutil.client.gui.screens.BlockEntityScreen;

import java.util.Objects;

public abstract class COMPBaseScreen extends BlockEntityScreen {
    public Identifier WALLPAPER = ComputerData.getWallpapers().get(0).getTexture();
    public COMPBaseScreen(Text title) {
        super(title);
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
        for(ComputerApp app : ComputerData.getApps()){
            if(Objects.equals(app.getName(), currentScreen)) {
                this.getNbtData().putString("Window", currentScreen);
                break;
            }
            else {
                this.getNbtData().putString("Window", "default");
            }
        }
    }

    @Override
    public void tick() {

        if(!(MinecraftClient.getInstance().world.getBlockEntity(getBlockPos()) instanceof ComputerBlockEntity)){
            close();
        }

        doubleClickingTime = doubleClickingTime > 0 ? doubleClickingTime - 1 : 0;
        doubleClicking = doubleClickingTime > 0;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(!doubleClicking) {
            doubleClickingTime = 10;
            doubleClicking = true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void changeWallpaper(String name){
        getNbtData().putString("wallpaper", name);
        saveData();

        for(ComputerData.Wallpaper wallpaper : ComputerData.getWallpapers()) {
            if(Objects.equals(wallpaper.getId(), getNbtData().getString("wallpaper"))){
                WALLPAPER = wallpaper.getTexture();
            }
        }
    }

    public boolean isDoubleClicking(){
        return doubleClicking;
    }

    public void saveData(){
        for (ComputerApp app: ComputerData.getApps()) {
            if(app.getScreen().getClass() == this.getClass()){
                this.updateIndex(app.getName());
                GoopyScreens.saveNbtFromScreen(getNbtData(), getBlockPos());
                return;
            }
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
