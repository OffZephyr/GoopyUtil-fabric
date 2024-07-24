package net.zephyr.goopyutil.item.tools;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlock;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockEntity;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockModel;
import net.zephyr.goopyutil.client.ClientHook;
import net.zephyr.goopyutil.init.BlockInit;

public class PaintbrushItem extends Item {
    public PaintbrushItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if(world.isClient()){
            if(world.getBlockState(context.getBlockPos()).isOf(BlockInit.LAYERED_BLOCK_BASE)){

                BlockState state = world.getBlockState(context.getBlockPos());
                int rotationAmount = LayeredBlockModel.getDirectionId(state.get(LayeredBlock.FACING));
                Direction directionClicked = context.getSide();
                Direction textureRotation = directionClicked;
                if(directionClicked != Direction.UP && directionClicked != Direction.DOWN) {
                    for (int j = 0; j < rotationAmount; j++) {
                        textureRotation = textureRotation.rotateYClockwise();
                    }
                }

                byte direction = (byte)LayeredBlockModel.getDirectionId(textureRotation);

                if(world.getBlockEntity(context.getBlockPos()) instanceof LayeredBlockEntity entity) {
                    NbtCompound data = entity.getCustomData();
                    data.putByte("editSide", direction);

                    ClientHook.openScreen("paintbrush", data, context.getBlockPos().asLong());

                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.useOnBlock(context);
    }
}
