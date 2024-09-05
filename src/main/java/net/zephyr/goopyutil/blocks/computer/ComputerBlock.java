package net.zephyr.goopyutil.blocks.computer;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.blocks.GoopyBlockWithEntity;
import net.zephyr.goopyutil.init.BlockEntityInit;
import net.zephyr.goopyutil.init.ItemInit;
import net.zephyr.goopyutil.util.ScreenUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ComputerBlock extends GoopyBlockWithEntity implements BlockEntityProvider {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public ComputerBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) { return true;}

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ComputerBlockEntity(pos, state);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof ComputerBlockEntity ent) {
            NbtCompound nbt =  ent.getCustomData().copy();
            nbt.putString("wallpaper", "blue_checker");
            ent.putCustomData(nbt);
        }
    }
    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getPickStack(world, pos, state);
        world.getBlockEntity(pos, BlockEntityInit.COMPUTER).ifPresent((blockEntity) -> {
            blockEntity.setStackNbt(itemStack, world.getRegistryManager());
        });
        return itemStack;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        ItemStack itemStack = new ItemStack(this);
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if(blockEntity instanceof GoopyBlockEntity ent){
            NbtCompound nbt = ent.getCustomData();
            itemStack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, existingNbt -> NbtComponent.of(existingNbt.copyNbt().copyFrom(nbt)));
        }
        List<ItemStack> item = new ArrayList<>();
        item.add(itemStack);
        return item;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        NbtCompound data = world.getBlockEntity(pos) instanceof ComputerBlockEntity ent ? ent.getCustomData() : new NbtCompound();

        if(player.getMainHandStack().isOf(ItemInit.FLOPPYDISK)){
            emptyDisk(data, world, pos, state);
            data.put("ai_data", player.getMainHandStack().encodeAllowEmpty(world.getRegistryManager()));
            player.getMainHandStack().decrementUnlessCreative(1, player);
            return ActionResult.SUCCESS;
        }
        else {
            if (!world.isClient()) {
                if (player instanceof ServerPlayerEntity p) {
                    ScreenUtils.openScreenOnServer(p, "computer_boot", pos, data);
                }
            }
        }
        return ActionResult.SUCCESS;
    }
    public void ejectFloppy(World world, BlockPos pos){
        NbtCompound data = world.getBlockEntity(pos) instanceof ComputerBlockEntity ent ? ent.getCustomData() : new NbtCompound();
        emptyDisk(data, world, pos, world.getBlockState(pos));
    }

    ActionResult emptyDisk(NbtCompound blockData, World world, BlockPos pos, BlockState state){
        ItemStack disk = ItemStack.fromNbtOrEmpty(world.getRegistryManager(), blockData.getCompound("ai_data"));

        if(disk != ItemStack.EMPTY) {
            Block.dropStack(world, pos, state.get(FACING), disk);
            blockData.put("ai_data", ItemStack.EMPTY.encodeAllowEmpty(world.getRegistryManager()));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

}
