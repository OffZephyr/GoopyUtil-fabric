package net.zephyr.goopyutil.mixin;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.camera_desk.CameraDeskBlockRenderer;
import net.zephyr.goopyutil.blocks.camera_desk.CameraRenderer;
import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import net.zephyr.goopyutil.util.IPostProcessorLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements IPostProcessorLoader {

    @Shadow
    MinecraftClient client;
    @Shadow
    PostEffectProcessor postProcessor;
    PostEffectProcessor monitorPostProcessor;
    private boolean monitorPostProcessorEnabled;
    @Shadow
    ResourceManager resourceManager;
    private Framebuffer monitorBuffer;

    @Shadow
    private void loadPostProcessor(Identifier id) {

    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    public void renderMonitorPostProcessor(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if(!client.skipGameRender) {
            renderMonitor(tickCounter.getLastDuration(), CameraRenderer.isDrawing());
        }
    }

    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
    public void getZoomLevel(CallbackInfoReturnable<Double> callbackInfo) {
        if(MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen) {
            double fov = 45;
            callbackInfo.setReturnValue(fov);
        }
    }
    @Inject(method = "onResized", at = @At(value = "HEAD"))
    private void illusions$onResized$HEAD(int width, int height, CallbackInfo ci) {
        if (this.monitorPostProcessor != null) {
            this.monitorPostProcessor.setupDimensions(width, height);
        }
        CameraRenderer.onResize(width, height);
    }

    @Override
    public void setPostProcessor(Identifier id) {
        loadPostProcessor(id);
    }

    @Override
    public void render(float delta) {
        if(postProcessor != null){
            postProcessor.render(delta);
        }
    }
    @Override
    public void setMonitorPostProcessor(Identifier id, Framebuffer framebuffer) {
        this.monitorBuffer = framebuffer;
        loadMonitorPostProcessor(id, framebuffer);
    }
    @Override
    public void renderMonitor(float delta, boolean bool) {
        if(bool && monitorPostProcessor != null){
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            monitorPostProcessor.render(delta);
        }
    }

    @Override
    public Framebuffer getActiveMonitorBuffer() {
        return this.monitorBuffer;
    }

    @Override
    public void clearPostProcessor() {
        if (this.postProcessor != null) {
            this.postProcessor.close();
        }
    }
    @Override
    public PostEffectProcessor getMonitorPostProcessor() {
        return this.monitorPostProcessor;
    }

    void loadMonitorPostProcessor(Identifier id, Framebuffer framebuffer){
        if (this.monitorPostProcessor != null) {
            this.monitorPostProcessor.close();
        }
        try {
            this.monitorPostProcessor = new PostEffectProcessor(this.client.getTextureManager(), this.resourceManager, framebuffer, id);
            this.monitorPostProcessor.setupDimensions(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());
            this.monitorPostProcessorEnabled = true;
        } catch (IOException iOException) {
            GoopyUtil.LOGGER.warn("Failed to load shader: {}", (Object)id, (Object)iOException);
            this.monitorPostProcessorEnabled = false;
        } catch (JsonSyntaxException jsonSyntaxException) {
            GoopyUtil.LOGGER.warn("Failed to parse shader: {}", (Object)id, (Object)jsonSyntaxException);
            this.monitorPostProcessorEnabled = false;
        }
    }
}
