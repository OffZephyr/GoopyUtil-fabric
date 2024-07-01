package net.zephyr.goopyutil.entity.zephyr;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.util.IEntityDataSaver;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.Objects;

public class ZephyrModel extends GeoModel<ZephyrEntity> {
    @Override
    public Identifier getModelResource(ZephyrEntity animatable) {
        String skin = ((IEntityDataSaver)animatable).getPersistentData().getString("Reskin");
        if(!animatable.getReskins().containsKey(skin)){
            return Identifier.of(GoopyUtil.MOD_ID, "geo/entity/zephyr/zephyr.geo.json");
        }
        return animatable.getReskins().get(skin)[1];
    }

    @Override
    public Identifier getTextureResource(ZephyrEntity animatable) {
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");
        if (!animatable.getReskins().containsKey(skin) || Objects.equals(skin, "")) {
            return Identifier.of(GoopyUtil.MOD_ID, "textures/entity/zephyr/zephyr.png");
        }
        return animatable.getReskins().get(skin)[0];
    }

    @Override
    public Identifier getAnimationResource(ZephyrEntity animatable) {
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");
        if (!animatable.getReskins().containsKey(skin)) {
            return Identifier.of(GoopyUtil.MOD_ID, "animations/entity/zephyr/zephyr.animation.json");
        }
        return animatable.getReskins().get(skin)[2];
    }

    @Override
    public void setCustomAnimations(ZephyrEntity animatable, long instanceId, AnimationState<ZephyrEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null && !((IEntityDataSaver)animatable).getPersistentData().getBoolean("Crawling")) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
