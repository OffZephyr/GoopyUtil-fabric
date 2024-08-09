package net.zephyr.goopyutil.entity.zephyr;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.zephyr.goopyutil.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.goopyutil.util.mixinAccessing.IGetClientManagers;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ZephyrModel extends GeoModel<ZephyrEntity> {
    @Override
    public Identifier getModelResource(ZephyrEntity animatable) {
        EntityDataManager manager = ((IGetClientManagers) MinecraftClient.getInstance()).getEntityDataManager();
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");

        if(manager.getSkin(animatable.getType(), "default") == null) return Identifier.of("");

        return manager.getSkin(animatable.getType(), skin).getGeo();
    }

    @Override
    public Identifier getTextureResource(ZephyrEntity animatable) {
        EntityDataManager manager = ((IGetClientManagers) MinecraftClient.getInstance()).getEntityDataManager();
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");

        if(manager.getSkin(animatable.getType(), "default") == null) return Identifier.of("");

        return manager.getSkin(animatable.getType(), skin).getTexture();
    }

    @Override
    public Identifier getAnimationResource(ZephyrEntity animatable) {
        EntityDataManager manager = ((IGetClientManagers) MinecraftClient.getInstance()).getEntityDataManager();
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");

        if(manager.getSkin(animatable.getType(), "default") == null) return Identifier.of("");

        return manager.getSkin(animatable.getType(), skin).getAnimations();
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
