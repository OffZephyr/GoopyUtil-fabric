package net.zephyr.goopyutil;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.zephyr.goopyutil.blocks.camera.CameraBlockRenderer;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockRenderer;
import net.zephyr.goopyutil.client.JavaModels;
import net.zephyr.goopyutil.client.gui.TabOverlayClass;
import net.zephyr.goopyutil.init.BlockEntityInit;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.networking.NetChannels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoopyUtilClient implements ClientModInitializer {
	public static final String MOD_ID = "goopyutil";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		NetChannels.registerS2CPackets();
		RegisterRenderers();

		HudRenderCallback.EVENT.register(new TabOverlayClass());
		LOGGER.info("Client Initialized.");
	}

	private void RegisterRenderers() {
		EntityModelLayerRegistry.registerModelLayer(JavaModels.LAYERED_BLOCK_FACE, LayeredBlockRenderer::getTexturedModelData);
		BlockEntityRendererFactories.register(BlockEntityInit.LAYERED_BLOCK, LayeredBlockRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(JavaModels.CAMERA_HEAD, CameraBlockRenderer::getTexturedModelData);
		BlockEntityRendererFactories.register(BlockEntityInit.CAMERA, CameraBlockRenderer::new);

		BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.CAMERA, RenderLayer.getCutout());
	}
}