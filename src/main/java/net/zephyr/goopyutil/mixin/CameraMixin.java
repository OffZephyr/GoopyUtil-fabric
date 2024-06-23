package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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

    @Overwrite
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        this.ready = true;
        this.area = area;
        this.focusedEntity = focusedEntity;
        this.thirdPerson = thirdPerson;
        this.lastTickDelta = tickDelta;
        if(MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen screen){
            Vec3d pos = screen.camPos();
            float yaw = screen.getYaw();
            float pitch = screen.getPitch();
            this.setPos(pos.x, pos.y, pos.z);
            this.setRotation(yaw, pitch);
            this.moveBy(this.clipToSpace(0.55f), 0.0f, 0.0f);
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
