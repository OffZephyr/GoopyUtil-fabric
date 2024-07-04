package net.zephyr.goopyutil.client.gui.screens.computer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.client.ClientHook;
import net.zephyr.goopyutil.util.Computer.ComputerApp;

import java.util.Objects;

public class COMPBootupScreen extends COMPBaseScreen {
    private Identifier BOOTUP_SCREEN = Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/boot_0.png");
    private int bootProgress = 0;
    private final int bootProgressLength = 40;

    public COMPBootupScreen(Text title, NbtCompound nbt, long l) {
        super(title, nbt, l);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void tick() {
        if(bootProgress < bootProgressLength) {
            bootProgress++;
        }
        else {
            setCurrentScreen();
        }

        if(bootProgress == 12)
            BOOTUP_SCREEN = Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/boot_1.png");
        if(bootProgress == 28)
            BOOTUP_SCREEN = Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/boot_2.png");
        if(bootProgress == 30)
            BOOTUP_SCREEN = Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/boot_3.png");
        if(bootProgress == 38)
            BOOTUP_SCREEN = Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/boot_4.png");
        super.tick();
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(BOOTUP_SCREEN, (width/2)-(screenSize/2), (height/2)-(screenSize/2), 0, 0, screenSize, screenSize, screenSize, screenSize);
        super.render(context, mouseX, mouseY, delta);
    }

    private void setCurrentScreen() {
        String window = this.getNbtData().getString("Window");

        if(!Objects.equals(window, "default")) {
            for(ComputerApp app : ComputerData.getApps()){
                if(Objects.equals(app.getName(), window)) {
                    ClientHook.openScreen(app.getName(), getNbtData(), getBlockPos().asLong());
                    return;
                }
            }
        }
        ClientHook.openScreen("desktop", getNbtData(), getBlockPos().asLong());
    }
}
