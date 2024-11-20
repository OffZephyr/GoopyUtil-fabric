package net.zephyr.goopyutil.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.arcademachine.ArcademachineBlockEntity;
import net.zephyr.goopyutil.blocks.camera.CameraBlockEntity;
import net.zephyr.goopyutil.blocks.camera_desk.CameraDeskBlockEntity;
import net.zephyr.goopyutil.blocks.computer.ComputerBlockEntity;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockEntity;
import net.zephyr.goopyutil.blocks.plushies.BephPlushieBlockEntity;

public class BlockEntityInit {
    public static BlockEntityType<ComputerBlockEntity> COMPUTER;
    public static BlockEntityType<LayeredBlockEntity> LAYERED_BLOCK;
    public static BlockEntityType<BephPlushieBlockEntity> BEPH_PLUSHIE;
    public static BlockEntityType<CameraBlockEntity> CAMERA;
    public static BlockEntityType<CameraDeskBlockEntity> CAMERA_DESK;
    public static BlockEntityType<ArcademachineBlockEntity> ARCADE_MACHINE;

    public static void registerBlockEntities() {
        COMPUTER =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(GoopyUtil.MOD_ID, "computer"),
                        FabricBlockEntityTypeBuilder.create(ComputerBlockEntity::new,
                                BlockInit.COMPUTER).build());
        LAYERED_BLOCK =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(GoopyUtil.MOD_ID, "layered_block"),
                        FabricBlockEntityTypeBuilder.create(LayeredBlockEntity::new,
                                BlockInit.LAYERED_BLOCK_BASE).build());
        BEPH_PLUSHIE =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(GoopyUtil.MOD_ID, "beph_plushie"),
                        FabricBlockEntityTypeBuilder.create(BephPlushieBlockEntity::new,
                                BlockInit.BEPH_PLUSHIE).build());

        CAMERA =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(GoopyUtil.MOD_ID, "camera"),
                        FabricBlockEntityTypeBuilder.create(CameraBlockEntity::new,
                                BlockInit.CAMERA).build());

        CAMERA_DESK =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(GoopyUtil.MOD_ID, "camera_desk"),
                        FabricBlockEntityTypeBuilder.create(CameraDeskBlockEntity::new,
                                BlockInit.CAMERA_DESK).build());

        ARCADE_MACHINE =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(GoopyUtil.MOD_ID, "arcade_machine"),
                        FabricBlockEntityTypeBuilder.create(ArcademachineBlockEntity::new,
                                BlockInit.CAMERA_DESK).build());


        GoopyUtil.LOGGER.info("Registering Block Entities for " + GoopyUtil.MOD_ID.toUpperCase());
    }
}
