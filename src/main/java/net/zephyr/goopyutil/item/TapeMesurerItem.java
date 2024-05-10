package net.zephyr.goopyutil.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockEntity;
import net.zephyr.goopyutil.entity.CameraMappingEntity;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.init.ItemInit;

import java.util.ArrayList;
import java.util.List;

public class TapeMesurerItem extends Item {
    private CameraMappingEntity map = null;
    public TapeMesurerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking() && !user.getOffHandStack().isOf(ItemInit.TABLET)) {
            resetTape(user.getStackInHand(hand), user, world);
            return TypedActionResult.success(user.getStackInHand(hand), true);
        }
        return super.use(world, user, hand);
    }
    void resetTape(ItemStack stack, PlayerEntity player, World world){
        if (stack.getNbt() == null) {
            NbtCompound data = new NbtCompound();
            stack.setNbt(data);
        }
        stack.getNbt().putBoolean("hasData", false);
        Text resetText = Text.translatable(GoopyUtil.MOD_ID + ".tape_measure.reset");
        player.sendMessage(resetText, true);
        world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SPYGLASS_STOP_USING, SoundCategory.PLAYERS, 1, 1, true);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if(!context.getPlayer().getOffHandStack().isEmpty() && context.getPlayer().getOffHandStack().getItem() instanceof TabletItem){
            boolean hasCorner = context.getStack().getOrCreateNbt().getBoolean("hasCorner");
            if (context.getPlayer().isSneaking()) {
                if(hasCorner){
                    context.getStack().getOrCreateNbt().putBoolean("hasCorner", false);
                    context.getStack().getOrCreateNbt().putLong("setupCorner1", 0);
                    context.getStack().getOrCreateNbt().putLong("setupCorner2", 0);
                }
                else{
                    ItemStack tablet = context.getPlayer().getOffHandStack();
                    boolean bl = tablet.getOrCreateNbt().getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty();
                    if(!bl){
                        NbtList mapNbt = tablet.getOrCreateNbt().getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();
                        for(int i = 0; i < mapNbt.size(); i++){
                            if(mapNbt.get(i).getType() == NbtElement.LONG_ARRAY_TYPE){
                                long[] line = mapNbt.getLongArray(i);
                                BlockPos pos1 = BlockPos.fromLong(line[0]);
                                BlockPos pos2 = BlockPos.fromLong(line[1]);
                                Box box  = new Box(pos1, pos2).expand(0.05f);
                                if(box.contains(context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ())){
                                    mapNbt.remove(i);
                                    tablet.getOrCreateNbt().put("CamMap", mapNbt);
                                    return ActionResult.SUCCESS;
                                }
                            }
                        }
                    }
                }
            }
            else {
                if (!hasCorner) {
                    context.getStack().getOrCreateNbt().putBoolean("hasCorner", true);
                    context.getStack().getOrCreateNbt().putLong("setupCorner1", context.getBlockPos().asLong());
                } else {
                    ItemStack tablet = context.getPlayer().getOffHandStack();
                    boolean bl = tablet.getOrCreateNbt().getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty();
                    NbtList mapNbt = bl ? new NbtList() : tablet.getOrCreateNbt().getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();

                    long[] line = new long[]{
                            context.getStack().getOrCreateNbt().getLong("setupCorner1"),
                            context.getStack().getOrCreateNbt().getLong("setupCorner2")
                    };
                    NbtLongArray lineNbt = new NbtLongArray(line);
                    mapNbt.add(lineNbt);

                    tablet.getOrCreateNbt().put("CamMap", mapNbt);

                    context.getStack().getOrCreateNbt().putBoolean("hasCorner", false);
                }
            }
        }
        else {
            if (world.getBlockState(context.getBlockPos()).isOf(BlockInit.LAYERED_BLOCK_BASE)) {
                if (context.getPlayer().isSneaking()) {
                    resetTape(context.getStack(), context.getPlayer(), context.getWorld());
                    return ActionResult.PASS;
                }

                if (world.getBlockEntity(context.getBlockPos()) instanceof LayeredBlockEntity entity) {
                    copyPaste(context, entity);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        boolean flag1 = stack.getOrCreateNbt().getBoolean("hasData");
        boolean flag2 = entity instanceof PlayerEntity;
        Item item = ((PlayerEntity) entity).getOffHandStack().getItem();
        boolean flag3 = flag2 && item instanceof TabletItem && stack.getOrCreateNbt().getBoolean("hasCorner");

        if(map != null && !map.getBoundingBox().contract(5).contains(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ())){
            map.updatePosition(entity.getBlockPos().getX() + 0.5f, entity.getBlockPos().getY() - 20, entity.getBlockPos().getZ() + 0.5f);
        }


        if(flag2 && selected && item instanceof TabletItem && !stack.getOrCreateNbt().getBoolean("summon_entity")){
            if(!world.isClient()) {
                map = ((EntityType<CameraMappingEntity>) EntityType.get("goopyutil:camera_mapping").get()).create(world);
                BlockPos entPos = entity.getBlockPos();
                map.updatePosition(entPos.getX() + 0.5f, entPos.getY() - 20, entPos.getZ() + 0.5f);
                world.spawnEntity(map);
                stack.getOrCreateNbt().putInt("mapEntityID", map.getId());
                map.updateEntityData(map.getCustomData().copy(), map.getServer(), map.getId());
            }
            stack.getOrCreateNbt().putBoolean("summon_entity", true);
        }
        if(!flag2 || !selected || !(item instanceof TabletItem)) {
            stack.getOrCreateNbt().putBoolean("hasCorner", false);
            stack.getOrCreateNbt().putBoolean("summon_entity", false);
            if(map!=null) map.remove(Entity.RemovalReason.DISCARDED);
            map = null;
            stack.getOrCreateNbt().putInt("mapEntityID", 0);
        }

        stack.getOrCreateNbt().putBoolean("used", flag1 || flag3);

        boolean hasCorner = stack.getOrCreateNbt().getBoolean("hasCorner");
        if (entity instanceof PlayerEntity p) {
            if (hasCorner) {
                long corner1 = stack.getOrCreateNbt().getLong("setupCorner1");
                HitResult blockHit = entity.raycast(20.0, 0.0f, false);
                BlockPos pos = ((BlockHitResult)blockHit).getBlockPos();
                BlockPos startPos = BlockPos.fromLong(corner1);

                stack.getOrCreateNbt().putLong("setupCorner2", corner2(startPos, pos));

            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
    private void copyPaste(ItemUsageContext context, LayeredBlockEntity entity){
        World world = context.getWorld();

        byte direction;
        switch (context.getSide()) {
            default -> direction = 0;
            case WEST -> direction = 1;
            case SOUTH -> direction = 2;
            case EAST -> direction = 3;
            case UP -> direction = 4;
            case DOWN -> direction = 5;
        }

        if (context.getStack().getNbt() == null || !context.getStack().getNbt().getBoolean("hasData")) {
            NbtCompound data = new NbtCompound();
            data.putBoolean("hasData", true);
            data.putByte("editSide", direction);

            NbtCompound blockData = new NbtCompound();
            for (int i = 0; i < 3; i++) {
                blockData.putString("layer" + i, entity.getCustomData().getCompound("layer" + i).getString("" + direction));
                blockData.putString("layer" + i, entity.getCustomData().getCompound("layer" + i).getString("" + direction));
                blockData.putString("layer" + i, entity.getCustomData().getCompound("layer" + i).getString("" + direction));
                for (int j = 0; j < 3; j++) {
                    blockData.putInt(i + "_r_" + j, entity.getCustomData().getCompound("layer" + i).getInt(direction + "_" + j + "_r"));
                    blockData.putInt(i + "_g_" + j, entity.getCustomData().getCompound("layer" + i).getInt(direction + "_" + j + "_g"));
                    blockData.putInt(i + "_b_" + j, entity.getCustomData().getCompound("layer" + i).getInt(direction + "_" + j + "_b"));
                }
            }
            data.put("data", blockData);
            context.getStack().setNbt(data);
            PlayerEntity player = context.getPlayer();
            world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SPYGLASS_USE, SoundCategory.PLAYERS, 1, 1, true);
        } else {
            NbtCompound entityData = entity.getCustomData().copy();
            NbtCompound itemData = context.getStack().getNbt().getCompound("data");
            for (int i = 0; i < 3; i++) {

                NbtCompound layerData = entityData.getCompound("layer" + i);
                layerData.putString("" + direction, itemData.getString("layer" + i));
                for (int j = 0; j < 3; j++) {
                    layerData.putInt(direction + "_" + j + "_r", itemData.getInt(i + "_r_" + j));
                    layerData.putInt(direction + "_" + j + "_g", itemData.getInt(i + "_g_" + j));
                    layerData.putInt(direction + "_" + j + "_b", itemData.getInt(i + "_b_" + j));
                }
                entityData.put("layer" + i, layerData);
            }

            entity.putCustomData(entityData);
            PlayerEntity player = context.getPlayer();
            world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SPYGLASS_USE, SoundCategory.PLAYERS, 1, 1, true);
            world.playSound(context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1, 1, true);
        }
    }
    private long corner2(BlockPos start, BlockPos end){
        int distX = MathHelper.abs(end.getX() - start.getX());
        int distZ = MathHelper.abs(end.getZ() - start.getZ());

        BlockPos pos = end;
        if(distX > distZ){
            pos = new BlockPos(end.getX(), start.getY(), start.getZ());
        }
        else {
            pos = new BlockPos(start.getX(), start.getY(), pos.getZ());
        }
        return pos.asLong();
    }
}
