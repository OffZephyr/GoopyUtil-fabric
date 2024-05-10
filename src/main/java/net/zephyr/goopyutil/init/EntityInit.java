package net.zephyr.goopyutil.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.entity.CameraMappingEntity;

public class EntityInit {
    public static final EntityType<CameraMappingEntity> CAMERA_MAPPING = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(GoopyUtil.MOD_ID, "camera_mapping"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CameraMappingEntity::new)
                    .dimensions(EntityDimensions.changing(50, 40)).build());
}
