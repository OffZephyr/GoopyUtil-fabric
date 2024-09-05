package net.zephyr.goopyutil.client.gui.screens.killscreens;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.client.gui.screens.GoopyScreen;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.init.SoundsInit;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractGoopyUtilKillScreen extends GoopyScreen {
    private static final Identifier DRAFT_REPORT_ICON_TEXTURE = Identifier.ofVanilla("icon/draft_report");
    int Static = 0;
    public int StaticAmount = 4;
    public String staticTexturePath = "textures/gui/static/";
    int tickSinceDeath = 0;
    private final int entityID;
    private final boolean isHardcore;
    private boolean goBackToTitle = false;
    private Text scoreText;
    private final List<ButtonWidget> buttons = Lists.newArrayList();
    @Nullable
    private ButtonWidget titleScreenButton;
    public AbstractGoopyUtilKillScreen(Text text, NbtCompound nbtCompound, long l) {
        super(text);
        this.isHardcore = nbtCompound.getBoolean("isHardcore");
        this.entityID = l < Integer.MAX_VALUE ? (int) l : Integer.MAX_VALUE;
        MinecraftClient.getInstance().options.hudHidden = true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void tick() {
        this.tickSinceDeath++;
        this.Static = this.Static + 1 >= StaticAmount ? 0 : this.Static + 1;
        super.tick();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        Text text1 = goBackToTitle ? Text.translatable("deathScreen.spectate") : this.isHardcore ? Text.translatable("deathScreen.spectate") : Text.translatable("deathScreen.respawn");
        Text text2 = Text.translatable("deathScreen.titleScreen");

        boolean hovering1 = isOnButton(mouseX, mouseY, (int)(this.width - 10 - (textRenderer.getWidth(text1) * 1.25f)), this.height - 52, this.width, 26);
        boolean hovering2 = isOnButton(mouseX, mouseY, (int)(this.width - 10 - (textRenderer.getWidth(text2) * 1.25f)), this.height - 26, this.width, 31);

        if(goBackToTitle){
            if(hovering1) {
                MinecraftClient.getInstance().player.playSound(SoundsInit.CAM_SWITCH, 1, 1.15f);
                this.quitLevel();
            }
            if(hovering2){
                MinecraftClient.getInstance().player.playSound(SoundsInit.CAM_SWITCH, 1, 1.2f);
                goBackToTitle = false;
            }
        }
        else {
            if(hovering1) {
                MinecraftClient.getInstance().player.playSound(SoundsInit.CAM_SWITCH, 1, 1.2f);
                this.client.options.hudHidden = false;
                GoopyUtilEntity.jumpscareEntity = null;
                this.client.player.requestRespawn();
                this.close();
            }
            if(hovering2) {
                MinecraftClient.getInstance().player.playSound(SoundsInit.CAM_SWITCH, 1, 1.15f);
                goBackToTitle = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        final float transitionMin = 40;
        final float transitionMax = 100;

        Entity entity = GoopyUtilEntity.jumpscareEntity;
        int jumpscareLength = entity != null && MinecraftClient.getInstance().world.getEntityById(entityID) instanceof GoopyUtilEntity goopyutilEntity ? goopyutilEntity.JumpScareLength() : 0;
        if(this.tickSinceDeath < jumpscareLength) return;
        int animationTick = this.tickSinceDeath - jumpscareLength;

        context.fill(0, 0, this.width, this.height, 0, 0xFD000000);
        renderDeathImage(context, mouseX, mouseY, delta);

        Text deathMessage = entity == null ? Text.translatable("death.goopyutil.default_death") : Text.translatable("death.goopyutil.stabbed.by", entity.getName());

        if(animationTick >= transitionMin) {
            float textAlpha = animationTick > transitionMax ? 204f : MathHelper.lerp((animationTick - transitionMin) / (transitionMax - transitionMin), 5, 204);
            float buttonAlpha = animationTick < transitionMax ? textAlpha : 204f;

            int color = ColorHelper.Argb.getArgb((int) textAlpha, 255, 255, 255);
            int buttonColor = ColorHelper.Argb.getArgb((int) buttonAlpha, 255, 255, 255);

            Text text1 = goBackToTitle ? Text.translatable("death.goopyutil.confirm") : this.isHardcore ? Text.translatable("deathScreen.spectate") : Text.translatable("deathScreen.respawn");
            Text text2 = goBackToTitle ? Text.translatable("death.goopyutil.back") : Text.translatable("deathScreen.titleScreen");

            boolean hovering1 = isOnButton(mouseX, mouseY, (int)(this.width - 10 - (textRenderer.getWidth(text1) * 1.25f)), this.height - 52, this.width, 26);
            boolean hovering2 = isOnButton(mouseX, mouseY, (int)(this.width - 10 - (textRenderer.getWidth(text2) * 1.25f)), this.height - 26, this.width, 31);

            String Button1 = hovering1 ? "> " + text1.getString() : text1.getString();
            String Button2 = hovering2 ? "> " + text2.getString() : text2.getString();

            if(this.goBackToTitle) {
                Text goBack = Text.translatable("deathScreen.quit.confirm");
                drawResizableText(context, textRenderer, goBack, 1.25f, this.width - 10 - (textRenderer.getWidth(goBack) * 1.25f), this.height - 63, buttonColor, 0x00000000, false, false);
            }

            drawResizableText(context, textRenderer, Text.translatable("death.goopyutil.game_over"), 2f, 10, 10, color, 0x00000000, false, false);

            drawResizableText(context, textRenderer, Text.literal(Button2), 1.25f, this.width - 10 - (textRenderer.getWidth(Button2) * 1.25f), this.height - 21, buttonColor, 0x00000000, false, false);
            drawResizableText(context, textRenderer, Text.literal(Button1), 1.25f, this.width - 10 - (textRenderer.getWidth(Button1) * 1.25f), this.height - 42, buttonColor, 0x00000000, false, false);


            drawResizableText(context, textRenderer, deathMessage, 1.25f, 10, this.height - 21, color, 0x00000000, false, false);
        }

        Identifier staticTexture = Identifier.of(GoopyUtil.MOD_ID, staticTexturePath + this.Static + ".png");
        float alpha = animationTick < transitionMin ? 0.98f : animationTick > transitionMax ? 0.105f : MathHelper.lerp((animationTick - transitionMin)/(transitionMax - transitionMin), 0.98f, 0.11f);
        drawRecolorableTexture(context, staticTexture, 0, 0, 0, this.width, this.height, 0, 0, this.width, this.height, 1, 1, 1, alpha);
    }


    private void quitLevel() {
        if (this.client.world != null) {
            this.client.world.disconnect();
        }

        this.client.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
        this.client.setScreen(new TitleScreen());
    }

    public abstract void renderDeathImage(DrawContext context, int mouseX, int mouseY, float delta);
}
