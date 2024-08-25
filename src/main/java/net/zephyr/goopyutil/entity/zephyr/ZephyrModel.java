package net.zephyr.goopyutil.entity.zephyr;

import it.unimi.dsi.fastutil.doubles.Double2LongFunctions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.networking.payloads.GetNbtC2SPayload;
import net.zephyr.goopyutil.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.goopyutil.util.mixinAccessing.IGetClientManagers;
import org.joml.Vector3d;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.EasingType;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.function.DoubleToLongFunction;

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

        GeoBone camera = getAnimationProcessor().getBone("camera");

        if (camera != null && animatable.getId() == ((IEntityDataSaver)MinecraftClient.getInstance().player).getPersistentData().getInt("JumpscareID")) {
            long[] camPos = new long[6];
            camPos[0] = Double.valueOf(camera.getWorldPosition().x() * 10000000).longValue();
            camPos[1] = Double.valueOf(camera.getWorldPosition().y() * 10000000).longValue();
            camPos[2] = Double.valueOf(camera.getWorldPosition().z() * 10000000).longValue();
            camPos[3] = Double.valueOf((camera.getRotX() * MathHelper.DEGREES_PER_RADIAN) * 10000000).longValue();
            camPos[4] = Double.valueOf((camera.getRotY() * MathHelper.DEGREES_PER_RADIAN) * 10000000).longValue();
            camPos[5] = Double.valueOf((camera.getRotZ() * MathHelper.DEGREES_PER_RADIAN) * 10000000).longValue();
            ((IEntityDataSaver) animatable).getPersistentData().putLongArray("JumpScarePos", camPos);
            //animatable.JumpscareCamPos = camera.getWorldPosition();
            //animatable.JumpscareCamRot = new Vec3d(, , );
        }

        GeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null && !((IEntityDataSaver) animatable).getPersistentData().getBoolean("Crawling")) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
