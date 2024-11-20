package net.zephyr.goopyutil.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.zephyr.goopyutil.client.rendering.CameraMapRenderer;
import net.zephyr.goopyutil.client.rendering.PropPlacingRenderer;
import net.zephyr.goopyutil.entity.cameramap.CameraMappingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    CameraMapRenderer mapRenderer = new CameraMapRenderer();
    PropPlacingRenderer propPlacingRenderer = new PropPlacingRenderer();
    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci){
        mapRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        propPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
    }
}
