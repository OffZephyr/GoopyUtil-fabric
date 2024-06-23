package net.zephyr.goopyutil.entity.zephyr;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.zephyr.goopyutil.GoopyUtil;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ZephyrModel extends GeoModel<ZephyrEntity> {
    @Override
    public Identifier getModelResource(ZephyrEntity animatable) {
        return Identifier.of(GoopyUtil.MOD_ID, "geo/entity/zephyr/zephyr.geo.json");
    }

    @Override
    public Identifier getTextureResource(ZephyrEntity animatable) {
        return Identifier.of(GoopyUtil.MOD_ID, "textures/entity/zephyr/zephyr.png");
    }

    @Override
    public Identifier getAnimationResource(ZephyrEntity animatable) {
        return Identifier.of(GoopyUtil.MOD_ID, "animations/entity/zephyr/zephyr.animation.json");
    }

    @Override
    public void setCustomAnimations(ZephyrEntity animatable, long instanceId, AnimationState<ZephyrEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
