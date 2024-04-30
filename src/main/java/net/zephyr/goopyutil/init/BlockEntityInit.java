package net.zephyr.goopyutil.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.camera.CameraBlockEntity;
import net.zephyr.goopyutil.blocks.computer.ComputerBlockEntity;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockEntity;

public class BlockEntityInit {
    public static BlockEntityType<ComputerBlockEntity> COMPUTER;
    public static BlockEntityType<LayeredBlockEntity> LAYERED_BLOCK;
    public static BlockEntityType<CameraBlockEntity> CAMERA;

    public static void registerBlockEntities() {
        COMPUTER =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(GoopyUtil.MOD_ID, "computer"),
                        FabricBlockEntityTypeBuilder.create(ComputerBlockEntity::new,
                                BlockInit.COMPUTER).build());
        LAYERED_BLOCK =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(GoopyUtil.MOD_ID, "layered_block"),
                        FabricBlockEntityTypeBuilder.create(LayeredBlockEntity::new,
                                BlockInit.LAYERED_BLOCK_BASE).build());

        CAMERA =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(GoopyUtil.MOD_ID, "camera"),
                        FabricBlockEntityTypeBuilder.create(CameraBlockEntity::new,
                                BlockInit.CAMERA).build());


        GoopyUtil.LOGGER.info("Registering Block Entities for " + GoopyUtil.MOD_ID.toUpperCase());
    }
}
