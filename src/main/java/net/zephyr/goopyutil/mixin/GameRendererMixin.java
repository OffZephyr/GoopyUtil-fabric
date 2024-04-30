package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.zephyr.goopyutil.client.gui.CameraTabletScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    MinecraftClient client;
    @ModifyArg(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V"), index = 1)
    private Entity injected(Entity ent) {
        return this.client.getCameraEntity() == null || this.client.currentScreen instanceof CameraTabletScreen ? this.client.player : this.client.getCameraEntity();
    }
    @ModifyArg(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V"), index = 2)
    private boolean injected(boolean ent) {
        if (this.client.currentScreen instanceof CameraTabletScreen){
            return true;
        }
        else {
            return !this.client.options.getPerspective().isFirstPerson();
        }
    }
}
