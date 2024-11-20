package net.zephyr.goopyutil.entity.zephyr;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntityModel;
import net.zephyr.goopyutil.init.ScreensInit;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

public class ZephyrModel extends GoopyUtilEntityModel<ZephyrEntity> {
    @Override
    public Identifier getDefaultTexture() {
        return Identifier.of(GoopyUtil.MOD_ID, "textures/entity/zephyr/zephyr.png");
    }

    @Override
    public Identifier getDefaultGeoModel() {
        return Identifier.of(GoopyUtil.MOD_ID, "geo/entity/zephyr/zephyr.geo.json");
    }

    @Override
    public Identifier getDefaultAnimations() {
        return Identifier.of(GoopyUtil.MOD_ID, "animations/entity/zephyr/zephyr.animation.json");
    }

    @Override
    public String getDefaultKillScreenID() {
        return ScreensInit.ZEPHYR_KILLSCREEN;
    }

    @Override
    public void setCustomAnimations(ZephyrEntity animatable, long instanceId, AnimationState<ZephyrEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null && !animatable.crawling) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }

        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
