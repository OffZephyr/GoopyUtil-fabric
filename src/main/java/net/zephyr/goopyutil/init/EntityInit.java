package net.zephyr.goopyutil.init;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.client.JavaModels;
import net.zephyr.goopyutil.entity.cameramap.CameraMappingEntity;
import net.zephyr.goopyutil.entity.cameramap.CameraMappingEntityRenderer;
import net.zephyr.goopyutil.entity.zephyr.ZephyrEntity;
import net.zephyr.goopyutil.entity.zephyr.ZephyrRenderer;

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
                            .dimensions(EntityDimensions.fixed(0.65f, 1.65f).withEyeHeight(1.55f)).build()
            );

    public static void registerEntities(){
        FabricDefaultAttributeRegistry.register(EntityInit.ZEPHYR, ZephyrEntity.setAttributes());
        GoopyUtil.LOGGER.info("Registering Entities for " + GoopyUtil.MOD_ID.toUpperCase());
    }
    public static void registerEntitiesOnClient(){
        EntityModelLayerRegistry.registerModelLayer(JavaModels.ZEPHYR, CameraMappingEntityRenderer::getTexturedModelData);
        EntityRendererRegistry.register(EntityInit.ZEPHYR, ZephyrRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(JavaModels.CAMERA_MAP, CameraMappingEntityRenderer::getTexturedModelData);
        EntityRendererRegistry.register(EntityInit.CAMERA_MAPPING, CameraMappingEntityRenderer::new);

        GoopyUtil.LOGGER.info("Registering Entities on CLIENT for " + GoopyUtil.MOD_ID.toUpperCase());
    }
}
