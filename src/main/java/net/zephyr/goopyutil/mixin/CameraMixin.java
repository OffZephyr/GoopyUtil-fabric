package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPRemoteScreen;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.entity.zephyr.ZephyrModel;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.goopyutil.util.mixinAccessing.IPlayerCustomModel;
import net.zephyr.goopyutil.util.mixinAccessing.ISetCameraThirdPerson;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.Arrays;

@Mixin(Camera.class)
public class CameraMixin implements ISetCameraThirdPerson {

    boolean forceThirdPerson = false;
    @Shadow boolean ready;
    @Shadow BlockView area;
    @Shadow Entity focusedEntity;
    @Shadow boolean thirdPerson;
    @Shadow float cameraY;
    @Shadow float lastCameraY;
    @Shadow float pitch;
    @Shadow float yaw;
    float roll = 0;
    @Shadow float lastTickDelta;

    @Shadow
    void setRotation(float yaw, float pitch) {

    }
    @Shadow
    void setPos(double x, double y, double z) {

    }
    @Shadow
    void moveBy(float f, float g, float h) {

    }
    @Shadow
    float clipToSpace(float f) {
        return 0;
    }
    @Shadow
    private final Vector3f horizontalPlane = new Vector3f(0.0f, 0.0f, 1.0f);
    @Shadow
    private final Vector3f verticalPlane = new Vector3f(0.0f, 1.0f, 0.0f);
    @Shadow
    private final Vector3f diagonalPlane = new Vector3f(1.0f, 0.0f, 0.0f);

    @Shadow
    private final Quaternionf rotation = new Quaternionf();
    @Shadow
    private static final Vector3f HORIZONTAL = new Vector3f(0.0f, 0.0f, -1.0f);
    @Shadow
    private static final Vector3f VERTICAL = new Vector3f(0.0f, 1.0f, 0.0f);
    @Shadow
    private static final Vector3f DIAGONAL = new Vector3f(-1.0f, 0.0f, 0.0f);

    protected void setRotation(float yaw, float pitch, float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.rotation.rotationYXZ((float)Math.PI - yaw * ((float)Math.PI / 180), -pitch * ((float)Math.PI / 180), -roll * ((float)Math.PI / 180));
        HORIZONTAL.rotate(this.rotation, this.horizontalPlane);
        VERTICAL.rotate(this.rotation, this.verticalPlane);
        DIAGONAL.rotate(this.rotation, this.diagonalPlane);
    }

    @Inject(method = "updateEyeHeight", at = @At("HEAD"), cancellable = true)
    public void updateEyeHeight(CallbackInfo ci) {
        if(((IPlayerCustomModel)MinecraftClient.getInstance().player).getCurrentEntity() instanceof GoopyUtilEntity gu) {
            this.lastCameraY = this.cameraY;
            this.cameraY = this.cameraY + (gu.getBaseDimensions(gu.mimicPlayer.getPose()).eyeHeight() - this.cameraY) * 0.5F;
            ci.cancel();
        }
    }

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
        this.ready = true;
        this.area = area;
        this.focusedEntity = focusedEntity;
        this.thirdPerson = thirdPerson || this.forceThirdPerson;
        this.lastTickDelta = tickDelta;

        PlayerEntity player = MinecraftClient.getInstance().player;

        GoopyUtilEntity entity = GoopyUtilEntity.jumpscareEntity;

        if(MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen screen){
            Vec3d pos = screen.camPos();
            float yaw = screen.getYaw();
            float pitch = screen.getPitch();
            this.setPos(pos.x, pos.y, pos.z);
            this.setRotation(yaw, pitch);
            this.moveBy(this.clipToSpace(0.55f), 0.0f, 0.0f);

            info.cancel();
        }
        else if(player != null &&
                player.isDead() &&
                player.getRecentDamageSource() != null &&
                player.getRecentDamageSource().getAttacker() instanceof GoopyUtilEntity &&
                entity != null &&
                entity.hasJumpScare()
        ) {
            entity.model.updateCamPos(entity);
            long[] camPos = entity.JumpscareCamPos;
            Vector3d JumpscareCamPos = new Vector3d(camPos[0] / 10000000d, camPos[1] / 10000000d, camPos[2] / 10000000d);
            Vector3d JumpscareCamRot = new Vector3d(camPos[3] / 10000000d, camPos[4] / 10000000d, camPos[5] / 10000000d);

            this.setRotation(-(float)JumpscareCamRot.y + (entity.getBodyYaw() + 180), (float)JumpscareCamRot.x, (float)JumpscareCamRot.z);
            this.setPos((float)JumpscareCamPos.x, (float)JumpscareCamPos.y, (float)JumpscareCamPos.z);

            info.cancel();
        }
    }

    @Override
    public void setThirsPerson(boolean thirdPerson) {
        this.forceThirdPerson = thirdPerson;
    }
}
