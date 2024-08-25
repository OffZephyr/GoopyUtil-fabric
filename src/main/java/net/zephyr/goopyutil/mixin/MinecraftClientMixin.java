package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.camera_desk.CameraRenderer;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.goopyutil.util.jsonReaders.layered_block.LayeredBlockManager;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.goopyutil.util.mixinAccessing.IGetClientManagers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

/**
 * When drawing a mirror, always use the mirror's framebuffer instead of the normal one.
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IGetClientManagers {
	@Shadow
	ReloadableResourceManagerImpl resourceManager;

	@Shadow
	CompletableFuture<Void> reloadResources() {
		return null;
	}

	private LayeredBlockManager layerManager = new LayeredBlockManager();
	private EntityDataManager entityDataManager = new EntityDataManager();

	@Inject(method = "getFramebuffer", at = @At("HEAD"), cancellable = true)
	public void getFramebuffer(CallbackInfoReturnable<Framebuffer> cir) {
		if (CameraRenderer.isDrawing()) {
			var framebuffer = CameraRenderer.getFramebuffer();
			if (framebuffer != null) {
				cir.setReturnValue(framebuffer);
			}
		}
	}
	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	public void setScreen(CallbackInfo cir) {
		World world = MinecraftClient.getInstance().world;
		PlayerEntity player = ((MinecraftClient)(Object)this).player;

		if (player != null &&
				player.getRecentDamageSource() != null &&
				player.getRecentDamageSource().getAttacker() instanceof GoopyUtilEntity &&
				player.getWorld().getEntityById(((IEntityDataSaver) player).getPersistentData().getInt("JumpscareID")) instanceof GoopyUtilEntity entity &&
				entity.hasJumpScare()) {
			cir.cancel();
		}
	}

	// TODO Change location of inject to not need ro reload the resources on launch.
	@Inject(method = "<init>", at = @At("TAIL"))
	public void reloaders(CallbackInfo ci) {
		this.resourceManager.registerReloader(this.layerManager);
		this.resourceManager.registerReloader(this.entityDataManager);
		reloadResources(); //TODO WE NEED THIS GOOONNNEEEEE
	}

	@Override
	public LayeredBlockManager getLayerManager() {
		return this.layerManager;
	}

	@Override
	public EntityDataManager getEntityDataManager() {
		return this.entityDataManager;
	}
}
