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
import net.zephyr.goopyutil.entity.base.GoopyGeckoEntity;
import net.zephyr.goopyutil.entity.goals.ShouldActiveTargetGoal;
import net.zephyr.goopyutil.entity.goals.ShouldLookAroundGoal;
import net.zephyr.goopyutil.entity.goals.ShouldLookAtEntityGoal;
import net.zephyr.goopyutil.entity.goals.ShouldWanderAndCrawlGoal;
import software.bernie.geckolib.animation.RawAnimation;

public class ZephyrEntity extends GoopyGeckoEntity {
    public ZephyrEntity(EntityType<ZephyrEntity> type, World world) {
        super(type, world);
        addReskin("neon",
                Identifier.of(GoopyUtil.MOD_ID, "textures/entity/zephyr/zephyr_neon.png"),
                Identifier.of(GoopyUtil.MOD_ID, "geo/entity/zephyr/zephyr.geo.json"),
                Identifier.of(GoopyUtil.MOD_ID, "animations/entity/zephyr/zephyr.animation.json")
        );
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
        this.goalSelector.add(2, new ShouldWanderAndCrawlGoal(this, 1));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, PlayerEntity.class, 6f));
        this.goalSelector.add(3, new ShouldLookAtEntityGoal(this, GoopyGeckoEntity.class, 6f));
        this.goalSelector.add(4, new ShouldLookAroundGoal(this));
    }

    @Override
    public boolean canCrawl() {
        return true;
    }

    @Override
    public float crawlHeight() {
        return 0.65f;
    }

    @Override
    public String demoAnim() {
        return performingAnim();
    }

    @Override
    public String defaultIdleAnim() {
        return "animation.zephyr.dayidle";
    }

    @Override
    public String defaultWalkingAnim() {
        return "animation.zephyr.daywalk";
    }

    @Override
    public String defaultRunningAnim() {
        return "animation.zephyr.dayrun";
    }

    @Override
    public String performingAnim() {
        return "animation.zephyr.performance";
    }

    @Override
    public String deactivatingAnim() {
        return "animation.zephyr.deactivating";
    }

    @Override
    public String deactivatedAnim() {
        return "animation.zephyr.deactivated";
    }

    @Override
    public String activatingAnim() {
        return "animation.zephyr.activating";
    }

    @Override
    public String aggressiveIdleAnim() {
        return "animation.zephyr.nightidle";
    }

    @Override
    public String aggressiveWalkingAnim() {
        return "animation.zephyr.nightwalk";
    }

    @Override
    public String aggressiveRunningAnim() {
        return "animation.zephyr.nightrun";
    }

    @Override
    public String crawlingIdleAnim() {
        return "animation.zephyr.crawlidle";
    }

    @Override
    public String crawlingWalkingAnim() {
        return "animation.zephyr.crawling";
    }

    @Override
    public String JumpScareAnim() {
        return "animation.zephyr.jumpscare";
    }

    @Override
    public String attackAnim() {
        return "animation.zephyr.attack";
    }

    @Override
    public String dyingAnim() {
        return "animation.zephyr.death";
    }

    @Override
    public int deathLength() {
        return 60;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }
}
