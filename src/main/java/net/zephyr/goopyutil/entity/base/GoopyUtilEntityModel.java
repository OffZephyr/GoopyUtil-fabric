package net.zephyr.goopyutil.entity.base;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.goopyutil.util.mixinAccessing.IGetClientManagers;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.Arrays;

public abstract class GoopyUtilEntityModel<T extends GoopyUtilEntity> extends GeoModel<T> {
    @Override
    public Identifier getModelResource(T animatable) {
        EntityDataManager manager = ((IGetClientManagers) MinecraftClient.getInstance()).getEntityDataManager();
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");

        if(manager.getSkin(animatable.getType(), skin) == null) return getDefaultGeoModel();

        return manager.getSkin(animatable.getType(), skin).getGeo();
    }

    @Override
    public Identifier getTextureResource(T animatable) {
        EntityDataManager manager = ((IGetClientManagers) MinecraftClient.getInstance()).getEntityDataManager();
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");

        if(manager.getSkin(animatable.getType(), skin) == null) return getDefaultTexture();

        return manager.getSkin(animatable.getType(), skin).getTexture();
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        EntityDataManager manager = ((IGetClientManagers) MinecraftClient.getInstance()).getEntityDataManager();
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");

        if(manager.getSkin(animatable.getType(), skin) == null) return getDefaultAnimations();

        return manager.getSkin(animatable.getType(), skin).getAnimations();
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        GeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null && !((IEntityDataSaver) animatable).getPersistentData().getBoolean("Crawling")) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }

        animatable.setModel(this);
        updateCamPos(animatable);
    }

    public void updateCamPos(GoopyUtilEntity entity){
        EntityDataManager manager = ((IGetClientManagers) MinecraftClient.getInstance()).getEntityDataManager();
        String skin = ((IEntityDataSaver) entity).getPersistentData().getString("Reskin");

        if(manager.getSkin(entity.getType(), skin) == null) {
            entity.killScreenID = getDefaultKillScreenID();
        }
        else {
            entity.killScreenID = manager.getSkin(entity.getType(), skin).getKillScreenID();
        }
        GeoBone camera = getAnimationProcessor().getBone("camera");

        if (camera != null && GoopyUtilEntity.jumpscareEntity != null && entity.getId() == GoopyUtilEntity.jumpscareEntity.getId()) {
            long[] camPos = new long[6];
            camPos[0] = Double.valueOf(camera.getWorldPosition().x() * 10000000).longValue();
            camPos[1] = Double.valueOf(camera.getWorldPosition().y() * 10000000).longValue();
            camPos[2] = Double.valueOf(camera.getWorldPosition().z() * 10000000).longValue();
            camPos[3] = Double.valueOf((camera.getRotX() * MathHelper.DEGREES_PER_RADIAN) * 10000000).longValue();
            camPos[4] = Double.valueOf((camera.getRotY() * MathHelper.DEGREES_PER_RADIAN) * 10000000).longValue();
            camPos[5] = Double.valueOf((camera.getRotZ() * MathHelper.DEGREES_PER_RADIAN) * 10000000).longValue();
            entity.JumpscareCamPos = camPos;
        }
    }

    public abstract Identifier getDefaultTexture();
    public abstract Identifier getDefaultGeoModel();
    public abstract Identifier getDefaultAnimations();
    public String getDefaultKillScreenID() {
        return "death_goopy_default";
    };
}
