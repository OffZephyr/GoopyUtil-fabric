package net.zephyr.goopyutil.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.zephyr.goopyutil.init.EntityInit;

public class ZephyrSpawn extends EntitySpawnItem {
    public ZephyrSpawn(Settings settings) {
        super(settings);
    }

    @Override
    public EntityType<? extends LivingEntity> entity() {
        return EntityInit.ZEPHYR;
    }
}
