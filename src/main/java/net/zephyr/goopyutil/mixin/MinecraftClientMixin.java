package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.zephyr.goopyutil.blocks.camera_desk.CameraDeskBlockRenderer;
import net.zephyr.goopyutil.blocks.camera_desk.CameraRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * When drawing a mirror, always use the mirror's framebuffer instead of the normal one.
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Inject(method = "getFramebuffer", at = @At("HEAD"), cancellable = true)
	public void getFramebuffer(CallbackInfoReturnable<Framebuffer> cir) {
		if (CameraRenderer.isDrawing()) {
			var framebuffer = CameraRenderer.getFramebuffer();
			if (framebuffer != null) {
				cir.setReturnValue(framebuffer);
			}
		}
	}

}
