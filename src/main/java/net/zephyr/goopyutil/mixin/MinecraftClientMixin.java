package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.zephyr.goopyutil.blocks.camera_desk.CameraRenderer;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockManager;
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

	@Inject(method = "getFramebuffer", at = @At("HEAD"), cancellable = true)
	public void getFramebuffer(CallbackInfoReturnable<Framebuffer> cir) {
		if (CameraRenderer.isDrawing()) {
			var framebuffer = CameraRenderer.getFramebuffer();
			if (framebuffer != null) {
				cir.setReturnValue(framebuffer);
			}
		}
	}

	// TODO Change location of inject to not need ro reload the resources on launch.
	@Inject(method = "<init>", at = @At("TAIL"))
	public void reloaders(CallbackInfo ci) {
		this.resourceManager.registerReloader(this.layerManager);
		reloadResources(); //WE NEED THIS GOOONNNEEEEE
	}

	@Override
	public LayeredBlockManager getLayerManager() {
		return this.layerManager;
	}
}
