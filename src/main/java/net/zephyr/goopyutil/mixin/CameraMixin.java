package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.entity.zephyr.ZephyrModel;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.Arrays;

@Mixin(Camera.class)
public class CameraMixin {
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
    /**
     * @author zephyr
     * @reason FCK U
     */
    @Overwrite
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        this.ready = true;
        this.area = area;
        this.focusedEntity = focusedEntity;
        this.thirdPerson = thirdPerson;
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
        }
        else if(player != null &&
                player.isDead() &&
                player.getRecentDamageSource() != null &&
                player.getRecentDamageSource().getAttacker() instanceof GoopyUtilEntity &&
                entity != null &&
                entity.hasJumpScare()
        ) {
            long[] camPos = entity.JumpscareCamPos;
            Vector3d JumpscareCamPos = new Vector3d(camPos[0] / 10000000d, camPos[1] / 10000000d, camPos[2] / 10000000d);
            Vector3d JumpscareCamRot = new Vector3d(camPos[3] / 10000000d, camPos[4] / 10000000d, camPos[5] / 10000000d);

            this.setRotation(-(float)JumpscareCamRot.y + (entity.getBodyYaw() + 180), (float)JumpscareCamRot.x, (float)JumpscareCamRot.z);
            this.setPos((float)JumpscareCamPos.x, (float)JumpscareCamPos.y, (float)JumpscareCamPos.z);
        }
        else {
            this.setRotation(focusedEntity.getYaw(tickDelta), focusedEntity.getPitch(tickDelta));
            this.setPos(MathHelper.lerp((double) tickDelta, focusedEntity.prevX, focusedEntity.getX()), MathHelper.lerp((double) tickDelta, focusedEntity.prevY, focusedEntity.getY()) + (double) MathHelper.lerp(tickDelta, this.lastCameraY, this.cameraY), MathHelper.lerp((double) tickDelta, focusedEntity.prevZ, focusedEntity.getZ()));
            if (thirdPerson) {
                if (inverseView) {
                    this.setRotation(this.yaw + 180.0F, -this.pitch);
                }

                float var10000;
                if (focusedEntity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) focusedEntity;
                    var10000 = livingEntity.getScale();
                } else {
                    var10000 = 1.0F;
                }

                float f = var10000;
                this.moveBy(-this.clipToSpace(4.0F * f), 0.0F, 0.0F);
            } else if (focusedEntity instanceof LivingEntity && ((LivingEntity) focusedEntity).isSleeping()) {
                Direction direction = ((LivingEntity) focusedEntity).getSleepingDirection();
                this.setRotation(direction != null ? direction.asRotation() - 180.0F : 0.0F, 0.0F);
                this.moveBy(0.0F, 0.3F, 0.0F);
            }
        }
    }
}
