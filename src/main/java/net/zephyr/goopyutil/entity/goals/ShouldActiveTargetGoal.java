package net.zephyr.goopyutil.entity.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.init.ItemInit;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ShouldActiveTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
    protected final Class<T> targetClass;
    protected final int reciprocalChance;
    public ShouldActiveTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility, boolean checkCanNavigate) {
        super(mob, targetClass, checkVisibility, checkCanNavigate);
        this.targetClass = targetClass;
        this.reciprocalChance = ActiveTargetGoal.toGoalTicks(10);
        this.setControls(EnumSet.of(Goal.Control.TARGET));
        this.targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange()).setPredicate(null);
    }


    @Override
    public boolean canStart() {
        if(mob instanceof GoopyUtilEntity entity) {
            boolean bl = entity.boolData(entity.getBehavior(), "aggressive", entity);

            boolean bl2 = true;
            if(targetEntity instanceof PlayerEntity player) {
                ItemStack stack = player.getInventory().armor.get(2);
                if(stack.isOf(ItemInit.ILLUSIONDISC)){
                    bl2 = false;
                }
            }

            return super.canStart() && bl && bl2;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if(mob instanceof GoopyUtilEntity entity) {
            boolean bl = entity.boolData(entity.getBehavior(), "aggressive", entity);

            boolean bl2 = true;
            if(targetEntity instanceof PlayerEntity player) {
                ItemStack stack = player.getInventory().armor.get(2);
                if(stack.isOf(ItemInit.ILLUSIONDISC)){
                    bl2 = false;
                }
            }

            return super.shouldContinue() && bl && bl2;
        }
        return false;
    }

    protected Box getSearchBox(double distance) {
        return this.mob.getBoundingBox().expand(distance, 4.0, distance);
    }

    protected void findClosestTarget() {
        this.targetEntity = this.targetClass == PlayerEntity.class || this.targetClass == ServerPlayerEntity.class ? this.mob.getWorld().getClosestPlayer(this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ()) : this.mob.getWorld().getClosestEntity(this.mob.getWorld().getEntitiesByClass(this.targetClass, this.getSearchBox(this.getFollowRange()), livingEntity -> true), this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }

    @Override
    protected boolean canTrack(@Nullable LivingEntity target, TargetPredicate targetPredicate) {
        return true;
    }
}
