package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.camera_desk.CameraRenderer;
import net.zephyr.goopyutil.client.ClientHook;
import net.zephyr.goopyutil.client.gui.screens.GoopyScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPRemoteScreen;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.util.ScreenUtils;
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
	public Screen currentScreen;

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
	public void setScreen(Screen screen, CallbackInfo cir) {
		MinecraftClient client = ((MinecraftClient) (Object) this);

		ClientPlayerEntity player = client.player;
		GoopyUtilEntity entity = GoopyUtilEntity.jumpscareEntity;


		if(player != null &&
				entity != null &&
				entity.hasJumpScare() )
		{
			if(MinecraftClient.getInstance().currentScreen instanceof GoopyScreen && screen != null){
				cir.cancel();
			}
		}

    }

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackManager;scanPacks()V"))
	public void reloaders(CallbackInfo ci) {
		this.resourceManager.registerReloader(this.layerManager);
		this.resourceManager.registerReloader(this.entityDataManager);
	}

	@Inject(method = "getCameraEntity", at = @At("HEAD"), cancellable = true)
	public void getCameraEntity(CallbackInfoReturnable<Entity> cir) {
		MinecraftClient client = ((MinecraftClient) (Object) this);
		if (client.currentScreen instanceof COMPRemoteScreen screen && screen.getControl() != null) {
			Entity entity = MinecraftClient.getInstance().player;
			Vec3d pos = entity.getPos();
			entity.setPosition(screen.getControl().getPos());
			cir.setReturnValue(entity);
			entity.setPosition(pos);
		}
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
