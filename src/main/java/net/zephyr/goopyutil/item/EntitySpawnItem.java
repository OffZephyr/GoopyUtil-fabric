package net.zephyr.goopyutil.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.util.IEntityDataSaver;

public abstract class EntitySpawnItem extends ItemWithDescription {
    public EntitySpawnItem(Settings settings, int... tools) {
        super(settings, ItemWithDescription.FLOPPY_DISK);
    }

    public abstract EntityType<? extends LivingEntity> entity();
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos().up();
        float yaw = context.getPlayerYaw() + 180f;
        LivingEntity entity = entity().create(context.getWorld());
        entity.setPosition(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
        entity.setBodyYaw(yaw);
        entity.setHeadYaw(yaw);
        ((IEntityDataSaver)entity).getPersistentData().putLong("spawnPos", pos.asLong());
        ((IEntityDataSaver)entity).getPersistentData().putFloat("spawnRot", yaw);
        context.getWorld().spawnEntity(entity);
        context.getStack().decrementUnlessCreative(1, context.getPlayer());
        return ActionResult.SUCCESS;
    }
}
