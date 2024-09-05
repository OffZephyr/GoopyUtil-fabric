package net.zephyr.goopyutil.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.goopyutil.client.ClientHook;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ClientPlayerEntityMixin{
    @Inject(method = "updatePostDeath", at = @At("HEAD"), cancellable = true)
    public void updatePostDeath(CallbackInfo info) {
        LivingEntity player = ((LivingEntity) (Object) this);
        GoopyUtilEntity entity = GoopyUtilEntity.jumpscareEntity;

        if (player.getRecentDamageSource() != null &&
                player.getRecentDamageSource().getAttacker() instanceof GoopyUtilEntity &&
                entity != null &&
                entity.hasJumpScare()) {
            ++player.deathTime;

            if (player.deathTime >= entity.JumpScareLength()) {
                player.remove(Entity.RemovalReason.KILLED);
            }
            info.cancel();
        }
    }
}
