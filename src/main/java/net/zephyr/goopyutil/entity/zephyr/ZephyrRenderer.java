package net.zephyr.goopyutil.entity.zephyr;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ZephyrRenderer extends GeoEntityRenderer<ZephyrEntity> {
    public ZephyrRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ZephyrModel());
        addRenderLayer(new NightGlowLayer<>(this));
    }

    @Override
    public void render(ZephyrEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void preRender(MatrixStack poseStack, ZephyrEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected float getDeathMaxRotation(ZephyrEntity animatable) {
        return 0.0F;
    }

    @Override
    public int getPackedOverlay(ZephyrEntity animatable, float u, float partialTick) {
        return OverlayTexture.packUv(OverlayTexture.getU(u),
                OverlayTexture.getV(false));
    }
}

