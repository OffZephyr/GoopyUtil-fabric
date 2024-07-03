package net.zephyr.goopyutil.entity.zephyr;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
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
import net.zephyr.goopyutil.entity.goals.CrawlerActiveTargetGoal;
import net.zephyr.goopyutil.entity.goals.WanderAndCrawlGoal;
import net.zephyr.goopyutil.util.IEntityDataSaver;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.RawAnimation;

public class ZephyrEntity extends GoopyGeckoEntity {
    public ZephyrEntity(EntityType<ZephyrEntity> type, World world) {
        super(type, world);
        IDLE_ANIM = RawAnimation.begin().thenLoop("animation.zephyr.dayidle");
        WALK_ANIM = RawAnimation.begin().thenLoop("animation.zephyr.daywalk");
        CRAWL_IDLE_ANIM = RawAnimation.begin().thenLoop("animation.zephyr.crawlidle");
        CRAWL_WALK_ANIM = RawAnimation.begin().thenLoop("animation.zephyr.crawling");

        canCrawl(true, 0.65f);

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
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12D);
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, false));
        this.targetSelector.add(1, new CrawlerActiveTargetGoal<>(this, PlayerEntity.class, false, true));
        this.goalSelector.add(2, new WanderAndCrawlGoal(this, 1));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6f));
        this.goalSelector.add(4, new LookAroundGoal(this));
    }
}
