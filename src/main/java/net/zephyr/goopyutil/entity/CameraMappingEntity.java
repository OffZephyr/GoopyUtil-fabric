package net.zephyr.goopyutil.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.zephyr.goopyutil.init.ItemInit;

public class CameraMappingEntity extends GoopyEntity {
    int delTimer = 60;
    public CameraMappingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        if(!getWorld().isClient()){
            boolean delete = true;
            for(ServerPlayerEntity p : PlayerLookup.tracking(this)){
                if(p.getMainHandStack().isOf(ItemInit.TAPEMEASURE)){
                    delete = false;
                }
            }
            delTimer = delete ? delTimer - 1 : 60;
            if (delTimer <= 0) remove(RemovalReason.DISCARDED);
        }
        super.tick();
    }
}
