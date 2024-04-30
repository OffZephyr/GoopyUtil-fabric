package net.zephyr.goopyutil.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockEntity;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.util.GoopyScreens;

public class TapeMesurerItem extends Item {
    public TapeMesurerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
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

        if (world.getBlockState(context.getBlockPos()).isOf(BlockInit.LAYERED_BLOCK_BASE)) {
            if(context.getPlayer().isSneaking()) {
                resetTape(context.getStack(), context.getPlayer(), context.getWorld());
                return ActionResult.PASS;
            }

            byte direction;
            switch (context.getSide()) {
                default -> direction = 0;
                case WEST -> direction = 1;
                case SOUTH -> direction = 2;
                case EAST -> direction = 3;
                case UP -> direction = 4;
                case DOWN -> direction = 5;
            }

            if (world.getBlockEntity(context.getBlockPos()) instanceof LayeredBlockEntity entity) {
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
                    return ActionResult.SUCCESS;
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
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if(stack.getNbt().getBoolean("hasData")){
            stack.getOrCreateNbt().putBoolean("used", true);
        }
        else {
            stack.getOrCreateNbt().putBoolean("used", false);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
