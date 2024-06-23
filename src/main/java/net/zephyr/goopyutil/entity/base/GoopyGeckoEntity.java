package net.zephyr.goopyutil.entity.base;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class GoopyGeckoEntity extends PathAwareEntity implements GeoEntity {
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected static RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("zephyr.animation.dayidle");
    protected static RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("zephyr.animation.daywalk");
    public GoopyGeckoEntity(EntityType<?> type, World world) {
        super((EntityType<? extends PathAwareEntity>) type, world);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Movement", 3, this::movementAnimController));
    }

    protected <E extends GoopyGeckoEntity> PlayState movementAnimController(final AnimationState<E> event) {
        if (event.isMoving())
            return event.setAndContinue(WALK_ANIM);
        else
            return event.setAndContinue(IDLE_ANIM);
        //return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
