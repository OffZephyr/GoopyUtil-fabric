package net.zephyr.goopyutil.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.camera.CameraBlockEntity;
import net.zephyr.goopyutil.client.gui.screens.CameraTabletScreen;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.util.GoopyScreens;

import java.util.ArrayList;
import java.util.List;

public class TabletItem extends Item {
    public TabletItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (hand == user.preferredHand)
            if (user instanceof ServerPlayerEntity p) {
                NbtCompound data = user.getMainHandStack().getOrCreateNbt();
                long[] cams = data.getLongArray("Cameras");
                List<Long> camsData = new ArrayList<>();
                for (int i = 0; i < cams.length; i++) {
                    camsData.add(cams[i]);
                }

                for (int j = 0; j < camsData.size(); j++) {
                    if (!(MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(camsData.get(j))) instanceof CameraBlockEntity)) {
                        camsData.remove(j);
                        data.putLongArray("Cameras", camsData);
                        GoopyScreens.saveNbtFromScreen(data);
                    }
                }

                boolean bl = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty();
                if(!bl) {
                    NbtList mapNbt = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();
                    int minX = Integer.MAX_VALUE;
                    int minZ = Integer.MAX_VALUE;
                    int maxX = Integer.MIN_VALUE;
                    int maxZ = Integer.MIN_VALUE;
                    for(int i = 0; i < mapNbt.size(); i++) {
                        if (mapNbt.get(i).getType() == NbtElement.LONG_ARRAY_TYPE) {
                            long[] line = mapNbt.getLongArray(i);
                            BlockPos pos1 = BlockPos.fromLong(line[0]);
                            BlockPos pos2 = BlockPos.fromLong(line[1]);
                            minX = Math.min(pos1.getX(), Math.min(pos2.getX(), minX));
                            minZ = Math.min(pos1.getZ(), Math.min(pos2.getZ(), minZ));
                        }
                    }
                    for(long cam : cams) {
                        BlockPos pos = BlockPos.fromLong(cam);
                        minX = Math.min(pos.getX(), minX);
                        minZ = Math.min(pos.getZ(), minZ);
                    }
                    for(int i = 0; i < mapNbt.size(); i++) {
                        if (mapNbt.get(i).getType() == NbtElement.LONG_ARRAY_TYPE) {
                            long[] line = mapNbt.getLongArray(i);
                            BlockPos pos1 = BlockPos.fromLong(line[0]);
                            BlockPos pos2 = BlockPos.fromLong(line[1]);
                            maxX = Math.max(pos1.getX(), Math.max(pos2.getX(), maxX));
                            maxZ = Math.max(pos1.getZ(), Math.max(pos2.getZ(), maxZ));
                        }
                    }
                    for(long cam : cams) {
                        BlockPos pos = BlockPos.fromLong(cam);
                        maxX = Math.max(pos.getX(), maxX);
                        maxZ = Math.max(pos.getZ(), maxZ);
                    }
                    BlockPos minPos = new BlockPos(minX, 0, minZ);
                    BlockPos maxPos = new BlockPos(maxX, 0, maxZ);
                    data.putLong("mapMinCorner", minPos.asLong());
                    data.putLong("mapMaxCorner", maxPos.asLong());
                }

                GoopyScreens.openScreenOnServer(p, "camera_tablet", user.getMainHandStack().getOrCreateNbt());
                return TypedActionResult.success(user.getStackInHand(hand), true);
            }
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        NbtCompound data = context.getStack().getOrCreateNbt();
        if (context.getStack() == context.getPlayer().getMainHandStack()) {

            if (world.isClient()) {
                if (world.getBlockState(context.getBlockPos()).isOf(BlockInit.CAMERA)) {
                    long[] cams = data.getLongArray("Cameras");
                    long pos = context.getBlockPos().asLong();
                    List<Long> camsData = new ArrayList<>();
                    for(int i = 0; i < cams.length; i++){
                        camsData.add(cams[i]);
                    }

                    if (context.getPlayer().isSneaking()) {
                        for (int i = 0; i < camsData.size(); i++) {
                            if(camsData.get(i) == pos){
                                camsData.remove(i);

                                data.putLongArray("Cameras", camsData);
                                GoopyScreens.saveNbtFromScreen(data);
                                return ActionResult.SUCCESS;
                            }
                        }

                        return ActionResult.PASS;
                    } else {
                        boolean shouldAdd = true;
                        for (Long cam : camsData) {
                            if (cam == pos) {
                                shouldAdd = false;
                            }
                        }
                        if(shouldAdd) {
                            camsData.add(pos);

                            data.putLongArray("Cameras", camsData);
                            GoopyScreens.saveNbtFromScreen(data);
                            return ActionResult.SUCCESS;
                        }
                        else {
                            return ActionResult.PASS;
                        }
                    }
                } else {
                    return ActionResult.PASS;
                }
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(world.isClient()){
            if(MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen screen && !stack.getOrCreateNbt().getBoolean("used")) {
                screen.setAsUsed(true);
            }
        }
        if(entity instanceof PlayerEntity p && p.getMainHandStack() != stack){
            stack.getOrCreateNbt().putBoolean("used", false);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
