package net.zephyr.goopyutil.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockEntity;
import net.zephyr.goopyutil.client.ClientHook;
import net.zephyr.goopyutil.entity.base.GoopyGeckoEntity;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.util.GoopyScreens;
import net.zephyr.goopyutil.util.IEntityDataSaver;

import java.util.List;
import java.util.Objects;

public class PaintbrushItem extends Item {
    public PaintbrushItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if(world.isClient()){
            if(world.getBlockState(context.getBlockPos()).isOf(BlockInit.LAYERED_BLOCK_BASE)){
                byte direction;
                switch (context.getSide()){
                    default -> direction = 0;
                    case WEST -> direction = 1;
                    case SOUTH -> direction = 2;
                    case EAST -> direction = 3;
                    case UP -> direction = 4;
                    case DOWN -> direction = 5;
                }

                if(world.getBlockEntity(context.getBlockPos()) instanceof LayeredBlockEntity entity) {
                    NbtCompound data = entity.getCustomData();
                    data.putByte("editSide", direction);

                    ClientHook.openScreen(GoopyScreens.getScreens().get("paintbrush"), context.getBlockPos(), data);

                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(entity instanceof GoopyGeckoEntity){
            String skin = ((IEntityDataSaver)entity).getPersistentData().getString("Reskin");
            if (Objects.equals(skin, "neon")) ((IEntityDataSaver)entity).getPersistentData().putString("Reskin", "");
            else ((IEntityDataSaver)entity).getPersistentData().putString("Reskin", "neon");
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
