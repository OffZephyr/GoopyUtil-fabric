package net.zephyr.goopyutil.client.gui.screens;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.types.Type;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.util.GoopyScreens;
import org.joml.Matrix4f;

import java.util.Set;

public abstract class GoopyScreen extends Screen {
    NbtCompound nbtData = new NbtCompound();
    public void putNbtData(NbtCompound nbt){
        nbtData = nbt.copy();
    }
    public NbtCompound getNbtData(){
        return nbtData;
    }
    public GoopyScreen(Text title) {
        super(title);
    }

    public boolean isOnButton(double mouseX, double mouseY, int x, int y, int width, int height) {
        return (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height);
    }

    public void renderButton(Identifier texture, DrawContext context, int x, int y, int u, int v, int u2, int v2, int width, int height, int textureWidth, int textureHeight, int mouseX, int mouseY){
        if(isOnButton(mouseX, mouseY, x, y, width, height)){
            context.drawTexture(texture, x, y, u2, v2, width, height, textureWidth, textureHeight);
        }
        else {
            context.drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    void drawRecolorableTexture(DrawContext context, Identifier texture, int x, int y, int z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight, float red, float green, float blue, float alpha) {
        float u1 = (u + 0.0f) /textureWidth;
        float u2 = (u + regionWidth) / textureWidth;
        float v1 = (v + 0.0f) / textureHeight;
        float v2 =  (v + regionHeight) / textureHeight;

        int x1 = x;
        int y1 = y;
        int x2 = x + (int)regionWidth;
        int y2 = y + (int)regionHeight;
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z).texture(u1, v1).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y2, (float)z).texture(u1, v2).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z).texture(u2, v2).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y1, (float)z).texture(u2, v1).color(red, green, blue, alpha);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    void drawResizableText(DrawContext context, TextRenderer textRenderer, Text text, float scale, float x, float y, int color, int backgroundColor, boolean shadow, boolean centered){

        x = x / scale;
        y = y / scale;
        if(centered) x -= (textRenderer.getWidth(text) / 2f);

        MatrixStack matrices = context.getMatrices();
        VertexConsumerProvider verticies = context.getVertexConsumers();

        matrices.push();
        matrices.scale(scale, scale, scale);
        textRenderer.draw(text, x, y, color, shadow, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, backgroundColor, 0xF000F0);
        matrices.scale(1 / scale, 1 / scale, 1 / scale);
    }
}
