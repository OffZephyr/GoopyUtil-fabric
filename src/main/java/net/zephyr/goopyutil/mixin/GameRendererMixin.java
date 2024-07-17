package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import net.zephyr.goopyutil.util.IPostProcessorLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements IPostProcessorLoader {

    @Shadow
    MinecraftClient client;
    @Shadow
    PostEffectProcessor postProcessor;

    @Shadow
    private void loadPostProcessor(Identifier id) {

    }

    @Shadow
    float zoom;
    /*@ModifyArg(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V"), index = 1)
    private Entity injected(Entity ent) {
        return this.client.getCameraEntity() == null || this.client.currentScreen instanceof CameraTabletScreen ? this.client.player : this.client.getCameraEntity();
    }*/
    /*@ModifyArg(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V"), index = 2)
    private boolean injected(boolean ent) {
        if (this.client.currentScreen instanceof CameraTabletScreen){
            return true;
        }
        else {
            return !this.client.options.getPerspective().isFirstPerson();
        }
    }*/
    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
    public void getZoomLevel(CallbackInfoReturnable<Double> callbackInfo) {
        if(MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen) {
            double fov = 45;
            callbackInfo.setReturnValue(fov);
        }
    }


    @Override
    public void setPostProcessor(Identifier id) {
        loadPostProcessor(id);
    }

    @Override
    public void clearPostProcessor() {
        if (this.postProcessor != null) {
            this.postProcessor.close();
        }
    }
}
