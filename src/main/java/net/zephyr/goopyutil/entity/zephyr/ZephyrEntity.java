package net.zephyr.goopyutil.entity.zephyr;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.entity.goals.ShouldActiveTargetGoal;
import net.zephyr.goopyutil.entity.goals.ShouldLookAroundGoal;
import net.zephyr.goopyutil.entity.goals.ShouldLookAtEntityGoal;
import net.zephyr.goopyutil.entity.goals.ShouldWanderGoal;

public class ZephyrEntity extends GoopyUtilEntity {
    public ZephyrEntity(EntityType<ZephyrEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {

        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.25f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12D);
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.add(1, new ShouldActiveTargetGoal<>(this, PlayerEntity.class, false, true));
        this.goalSelector.add(2, new ShouldWanderGoal(this, 1));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, PlayerEntity.class, 6f));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, GoopyUtilEntity.class, 6f));
        this.goalSelector.add(4, new ShouldLookAroundGoal(this));
    }

    @Override
    public String demoAnim() {
        return performingAnim();
    }

    public String defaultIdleAnim() {
        return "animation.zephyr.dayidle";
    }

    public String defaultWalkingAnim() {
        return "animation.zephyr.daywalk";
    }

    public String defaultRunningAnim() {
        return "animation.zephyr.dayrun";
    }

    public String performingAnim() {
        return "animation.zephyr.performance";
    }

    public String deactivatingAnim() {
        return "animation.zephyr.deactivating";
    }

    public String deactivatedAnim() {
        return "animation.zephyr.deactivated";
    }

    public String activatingAnim() {
        return "animation.zephyr.activating";
    }

    public String aggressiveIdleAnim() {
        return "animation.zephyr.nightidle";
    }

    public String aggressiveWalkingAnim() {
        return "animation.zephyr.nightwalk";
    }

    public String aggressiveRunningAnim() {
        return "animation.zephyr.nightrun";
    }

    public String crawlingIdleAnim() {
        return "animation.zephyr.crawlidle";
    }

    public String crawlingWalkingAnim() {
        return "animation.zephyr.crawling";
    }

    public String JumpScareAnim() {
        return "animation.zephyr.jumpscare";
    }

    public String attackAnim() {
        return "animation.zephyr.attack";
    }

    public String dyingAnim() {
        return "animation.zephyr.death";
    }

    public int deathLength() {
        return 60;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }
}
