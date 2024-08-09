package net.zephyr.goopyutil.entity.goals;

import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.mob.MobEntity;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;

public class ShouldLookAroundGoal extends LookAroundGoal {
    private final MobEntity mob;

    public ShouldLookAroundGoal(MobEntity mob) {
        super(mob);
        this.mob = mob;
    }


    @Override
    public boolean canStart() {
        if(mob instanceof GoopyUtilEntity entity) {
            boolean bl = entity.boolData(entity.getBehavior(), "look_around", entity);
            return super.canStart() && bl;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if(mob instanceof GoopyUtilEntity entity) {
            boolean bl = entity.boolData(entity.getBehavior(), "look_around", entity);
            return super.shouldContinue() && bl;
        }
        return false;
    }
}
