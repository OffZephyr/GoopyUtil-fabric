package net.zephyr.goopyutil.item.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.camera.CameraBlockEntity;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.util.ScreenUtils;

public class WrenchItem extends Item {
    public WrenchItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.getBlockState(context.getBlockPos()).isOf(BlockInit.CAMERA)) {

            if (world.getBlockEntity(context.getBlockPos()) instanceof CameraBlockEntity entity) {
                NbtCompound data = entity.getCustomData();

                if (context.getPlayer() instanceof ServerPlayerEntity p) {
                    ScreenUtils.openScreenOnServer(p, "camera_edit", context.getBlockPos(), data);
                }/* else {
                ClientHook.openScreen(GoopyScreens.getScreens().get("camera_edit"), context.getBlockPos(), data);
                }*/
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnBlock(context);
    }
}
