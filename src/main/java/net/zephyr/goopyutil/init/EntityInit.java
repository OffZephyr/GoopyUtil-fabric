package net.zephyr.goopyutil.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.entity.cameramap.CameraMappingEntity;
import net.zephyr.goopyutil.entity.zephyr.ZephyrEntity;

public class EntityInit {
    public static final EntityType<CameraMappingEntity> CAMERA_MAPPING =
            Registry.register(
                    Registries.ENTITY_TYPE, Identifier.of(GoopyUtil.MOD_ID, "camera_mapping"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, CameraMappingEntity::new)
                            .dimensions(EntityDimensions.fixed(50, 40)).build()
            );
    public static final EntityType<ZephyrEntity> ZEPHYR =
            Registry.register(
                    Registries.ENTITY_TYPE, Identifier.of(GoopyUtil.MOD_ID, "zephyr"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, ZephyrEntity::new)
                            .dimensions(EntityDimensions.fixed(0.65f, 1.65f)).build()
            );

    public static void registerEntities(){
        FabricDefaultAttributeRegistry.register(EntityInit.ZEPHYR, ZephyrEntity.setAttributes());
        GoopyUtil.LOGGER.info("Registering Entities for " + GoopyUtil.MOD_ID.toUpperCase());
    }
}
