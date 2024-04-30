package net.zephyr.goopyutil.blocks.camera;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.init.BlockEntityInit;

public class CameraBlockEntity extends GoopyBlockEntity {
    final float cameraBaseSpeed = 0.416f;
    public CameraBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CAMERA, pos, state);
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState state, GoopyBlockEntity entity) {
        NbtCompound data = getCustomData();
        if(data.getByte("ModeX") == 1){
            byte speedX = data.getByte("yawSpeed");

            float offset = (cameraBaseSpeed * (speedX+1));
            offset = data.getBoolean("panningXReverse") ? -offset : offset;

            if(data.getFloat("panningXProgress") >= 150) data.putBoolean("panningXReverse", true);
            else if(data.getFloat("panningXProgress") <= -50) data.putBoolean("panningXReverse", false);

            data.putFloat("panningXProgress", data.getFloat("panningXProgress") + offset);


            float panningX = data.getFloat("panningXProgress") > 100 ? 100 : data.getFloat("panningXProgress") < 0 ? 0 : data.getFloat("panningXProgress");
            double minX = data.getFloat("minYaw");
            double maxX = data.getFloat("maxYaw");
            double newYaw = panningX/100 * (maxX - minX);
            double endYaw = minX + newYaw;

            data.putDouble("yaw", -endYaw);
        }
        if(data.getByte("ModeY") == 1){
            byte speedX = data.getByte("pitchSpeed");

            float offset = (cameraBaseSpeed * (speedX+1));
            offset = data.getBoolean("panningYReverse") ? -offset : offset;

            if(data.getFloat("panningYProgress") >= 150) data.putBoolean("panningYReverse", true);
            else if(data.getFloat("panningYProgress") <= -50) data.putBoolean("panningYReverse", false);

            data.putFloat("panningYProgress", data.getFloat("panningYProgress") + offset);


            float panningY = data.getFloat("panningYProgress") > 100 ? 100 : data.getFloat("panningYProgress") < 0 ? 0 : data.getFloat("panningYProgress");
            double minY = data.getFloat("minPitch");
            double maxY = data.getFloat("maxPitch");
            double newPitch = panningY/100 * (maxY - minY);
            double endPitch = minY + newPitch;

            data.putDouble("pitch", -endPitch);
        }

        if(state.get(CameraBlock.LIT) != getCustomData().getBoolean("Lit")) {
            world.setBlockState(blockPos, state.with(CameraBlock.LIT, getCustomData().getBoolean("Lit")), 3);
        }
        super.tick(world, blockPos, state, entity);
    }
}
