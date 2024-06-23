package net.zephyr.goopyutil.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockEntity;
import net.zephyr.goopyutil.client.ClientHook;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.util.GoopyScreens;

import java.util.List;

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
                }
            }
        }
        return super.useOnBlock(context);
    }
}
