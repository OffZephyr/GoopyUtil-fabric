package net.zephyr.goopyutil.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.shape.VoxelShape;
import net.zephyr.goopyutil.blocks.OffsetRotationBlock;
import net.zephyr.goopyutil.blocks.camera_desk.CameraDeskBlockRenderer;
import net.zephyr.goopyutil.blocks.camera_desk.CameraRenderer;
import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import net.zephyr.goopyutil.client.rendering.CameraMapRenderer;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    private ClientWorld world;
    @Shadow
    private static void drawCuboidShapeOutline(
            MatrixStack matrices,
            VertexConsumer vertexConsumer,
            VoxelShape shape,
            double offsetX,
            double offsetY,
            double offsetZ,
            float red,
            float green,
            float blue,
            float alpha
    ){};

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;isThirdPerson()Z"
            )
    )

    public boolean Goopy_player_isThirdPerson(Camera camera) {
        return camera.isThirdPerson()|| CameraRenderer.isDrawing() || MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen;
    }


    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    public void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, CallbackInfo ci){
        if(state.getBlock() instanceof OffsetRotationBlock) {
            OffsetRotationBlock.drawingOutline = true;
            matrices.push();
            float rotation = ((IEntityDataSaver)this.world.getBlockEntity(pos)).getPersistentData().getFloat("Rotation") + 180;

            double offsetX = (state.get(OffsetRotationBlock.OFFSET_X) / 16f);
            double offsetY = (state.get(OffsetRotationBlock.OFFSET_Y) / 16f);
            double offsetZ = (state.get(OffsetRotationBlock.OFFSET_Z) / 16f);

            double posX = pos.getX();
            double posY = pos.getY();
            double posZ = pos.getZ();

            matrices.translate(offsetX + posX, offsetY + posY, offsetZ + posZ);

            matrices.translate(-cameraX,-cameraY,-cameraZ);
            matrices.translate(-0.5,0,-0.5);
            matrices.translate(0.5,0,0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation));
            matrices.translate(-0.5f, 0, -0.5f);

            drawCuboidShapeOutline(
                    matrices,
                    vertexConsumer,
                    state.getOutlineShape(this.world, pos, ShapeContext.of(entity)),
                    0,
                    0,
                    0,
                    0.0F,
                    0.0F,
                    0.0F,
                    0.4F
            );

            matrices.pop();
            OffsetRotationBlock.drawingOutline = false;
            ci.cancel();
        }
    }


}
