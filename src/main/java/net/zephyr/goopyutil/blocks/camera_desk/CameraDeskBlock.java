package net.zephyr.goopyutil.blocks.camera_desk;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.zephyr.goopyutil.blocks.GoopyBlockWithEntity;

public class CameraDeskBlock extends GoopyBlockWithEntity {
    public CameraDeskBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }
}
