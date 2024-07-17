package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;isThirdPerson()Z"
            )
    )
    public boolean illusions$render$isThirdPerson(Camera camera) {
        //return camera.isThirdPerson() || MirrorRenderer.isDrawing();
        return camera.isThirdPerson() || MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen;
    }

}
