package net.zephyr.goopyutil.blocks.camera_desk;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.blocks.camera.CameraBlock;
import net.zephyr.goopyutil.blocks.camera.CameraBlockEntity;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlock;
import net.zephyr.goopyutil.init.BlockEntityInit;
import org.joml.Matrix3f;

import java.util.ArrayList;
import java.util.List;

public class CameraDeskBlockEntity extends GoopyBlockEntity {
    public static List<BlockPos> posList= new ArrayList<>();
    private float animatedCounter = 0;
    private int curCamIndex = 0;
    private long currentCam = 0;
    boolean enableNightVision = false;
    private List<Long> cams = new ArrayList<>();
    NbtCompound camData = new NbtCompound();

    public CameraDeskBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CAMERA_DESK, pos, state);
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState state, GoopyBlockEntity entity) {
        updateViewport(this);
        super.tick(world, blockPos, state, entity);
    }

    public static void updateViewport(CameraDeskBlockEntity blockEntity) {
        if (!posList.contains(blockEntity.getPos())) {
            posList.add(blockEntity.getPos());
            System.out.println("CameraDeskBlockEntity added at " + blockEntity.getPos());
        }
    }

    public float getYaw(){
        return 90;
    }

    public float getFeedPitch(){
        if(hasFootage()) {
            return currentCamNbt(getWorld()).getFloat("pitch");
        }
        return 0;
    }
    public float getFeedYaw(){
        if(hasFootage()) {
            return currentCamNbt(getWorld()).getFloat("yaw") + getWorld().getBlockState(BlockPos.fromLong(currentCam)).get(LayeredBlock.FACING).asRotation();
        }
        return 0;
    }
    public Vec3d getFeedPos(){
        if (hasFootage()) {
            BlockPos pos = BlockPos.fromLong(currentCam);
            Vec3d offset;
            float amount = 0.2f;
            switch (MinecraftClient.getInstance().world.getBlockState(pos).get(CameraBlock.FACING)) {
                default -> offset = new Vec3d(0, 0, amount);
                case SOUTH -> offset = new Vec3d(0, 0, -amount);
                case EAST -> offset = new Vec3d(-amount, 0, 0);
                case WEST -> offset = new Vec3d(amount, 0, 0);
            }

            return new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.7f, pos.getZ() + 0.5f).add(offset);
        }
        return new Vec3d(0, 0, 0);
    }
    public boolean hasFootage(){
        BlockPos pos = BlockPos.fromLong(currentCam);
        return !camData.isEmpty() && !cams.isEmpty() && getWorld().getBlockEntity(pos) instanceof CameraBlockEntity;
    }

    public void updateCams(){
        camData = getCustomData().getCompound("cam_data");
        cams = new ArrayList<>();
        long[] camsData = camData.getLongArray("Cameras");
        for (long cam : camsData) {
            cams.add(cam);
        }
        curCamIndex = !cams.isEmpty() && curCamIndex + 1 < cams.size() ? curCamIndex + 1 : 0;
        currentCam = cams.get(curCamIndex);
    }

    public NbtCompound currentCamNbt(World world){
        if(world != null) {
            BlockPos pos = BlockPos.fromLong(currentCam);
            if (world.getBlockEntity(pos) instanceof CameraBlockEntity ent) {
                return ent.getCustomData();
            } else {
                currentCam = 0;
            }
        }
        return new NbtCompound();
    }

    public boolean isNightVision(){
        return hasFootage() && currentCamNbt(getWorld()).getByte("NightVision") == 1 || (currentCamNbt(getWorld()).getByte("NightVision") == 2 && this.enableNightVision);
    }
    public Matrix3f nightVisionColorFilter(){
        return new Matrix3f(0.5f, 0.5f, 0.5f, 0.75f, 0.75f, 0.75f, 0.65f, 0.65f, 0.65f);
    }
    public float screenSaturation(){
        float normal = 0.2f;
        float nv = 1.1f;
        return isNightVision() ? nv : normal;
    }

    Identifier staticTexture(float deltaTick){
        float maxAnimatedCounter = 180;
        animatedCounter = animatedCounter + deltaTick < maxAnimatedCounter ? animatedCounter + deltaTick : animatedCounter + deltaTick - maxAnimatedCounter;
        int num = animatedCounter < maxAnimatedCounter/4 ? 0 : animatedCounter < maxAnimatedCounter/2 ? 1 : animatedCounter < (maxAnimatedCounter/4) * 3 ? 2 : 3;
        return Identifier.of(GoopyUtil.MOD_ID, "textures/block/static/" + num + ".png");
    }
}
