package net.zephyr.goopyutil.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.init.ItemInit;
import net.zephyr.goopyutil.util.ItemNbtUtil;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.goopyutil.util.mixinAccessing.IPlayerCustomModel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements IPlayerCustomModel {
    boolean crawling = false;
    @Nullable GoopyUtilEntity currentEntity;
    float mimicBodyYaw = 0;
    @Inject (method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        ItemStack stack = player.getInventory().armor.get(2);
        if(stack.isOf(ItemInit.ILLUSIONDISC)){

            String animatronic = ItemNbtUtil.getNbt(stack).getString("entity");
            NbtCompound animatronicData = ItemNbtUtil.getNbt(stack).getCompound("entityData");
            if (!animatronic.isEmpty() && ComputerData.getAIAnimatronic(animatronic) instanceof ComputerData.Initializer.AnimatronicAI ai) {

                if (((IPlayerCustomModel) player).getCurrentEntity() == null || ((IPlayerCustomModel)player).getCurrentEntity().getType() != ai.entityType()) {
                    LivingEntity entity1 = ai.entityType().create(player.getWorld());
                    ((IEntityDataSaver) entity1).getPersistentData().copyFrom(animatronicData);
                    if (entity1 instanceof GoopyUtilEntity ent) {
                        ent.mimicPlayer = player;
                        ent.mimic = true;
                        ent.setAiDisabled(true);
                        setCurrentEntity(ent);
                    }
                }
            }
            if(getCurrentEntity() != null) {
                GoopyUtilEntity entity = getCurrentEntity();

                entity.tick();

                World world = entity.mimicPlayer.getWorld();
                entity.setCrawling(entity.mimicPlayer.isCrawling(), world);
                entity.setRunning(entity.mimicPlayer.isSprinting(), world);

                if(player.getAttacking() instanceof PlayerEntity p) {
                    ((IEntityDataSaver)p).getPersistentData().putInt("JumpscareID", entity.getId());
                }

                player.calculateDimensions();
            }
        }
        else {
            resetCurrentEntity();
        }
    }

    @Inject(method = "updatePose", at = @At("HEAD"), cancellable = true)
    public void updatePose(CallbackInfo ci){
        PlayerEntity player = ((PlayerEntity) (Object)this);
        if(shouldBeCrawling()) {
            player.setPose(EntityPose.SWIMMING);
            ci.cancel();
        }
    }

    @Inject(method = "getBaseDimensions", at = @At("HEAD"), cancellable = true)
    public void getBaseDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> ci){
        if(getCurrentEntity() != null) {
            ci.setReturnValue(getCurrentEntity().getBaseDimensions(pose));
        }
    }
    @Override
    public GoopyUtilEntity getCurrentEntity() {
        return currentEntity;
    }

    @Override
    public void setCurrentEntity(@Nullable GoopyUtilEntity entity) {
        currentEntity = entity;
    }
    @Override
    public void resetCurrentEntity() {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        if(currentEntity != null) {
            currentEntity.mimicPlayer.calculateDimensions();
            currentEntity.remove(Entity.RemovalReason.DISCARDED);
            currentEntity = null;
            player.calculateDimensions();
        }
    }

    @Override
    public float getMimicYaw() {
        return mimicBodyYaw;
    }

    @Override
    public void setMimicYaw(float yaw) {
        mimicBodyYaw = yaw;
    }

    @Override
    public boolean shouldBeCrawling() {
        return crawling;
    }

    @Override
    public void setCrawling(boolean crawling) {
        this.crawling = crawling;
    }
}
