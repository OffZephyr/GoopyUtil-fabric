package net.zephyr.goopyutil.entity.base;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPRemoteScreen;
import net.zephyr.goopyutil.entity.zephyr.NightGlowLayer;
import net.zephyr.goopyutil.entity.zephyr.ZephyrEntity;
import net.zephyr.goopyutil.entity.zephyr.ZephyrModel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public abstract class GoopyUtilEntityRenderer<T extends GoopyUtilEntity> extends GeoEntityRenderer<T> {
    public GoopyUtilEntityRenderer(EntityRendererFactory.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if(this.getGeoModel() instanceof GoopyUtilEntityModel<T> model){
            model.entityRenderer = this;
        }

        if(MinecraftClient.getInstance().player.isDead() && GoopyUtilEntity.jumpscareEntity != null && entity.getId() != GoopyUtilEntity.jumpscareEntity.getId()) return;

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void preRender(MatrixStack poseStack, T animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected float getDeathMaxRotation(T animatable) {
        return 0.0F;
    }

    @Override
    public int getPackedOverlay(T animatable, float u, float partialTick) {
        return OverlayTexture.packUv(OverlayTexture.getU(u),
                OverlayTexture.getV(false));
    }
}

