package net.zephyr.goopyutil;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.RenderLayer;
import net.zephyr.goopyutil.client.JavaModels;
import net.zephyr.goopyutil.client.gui.TabOverlayClass;
import net.zephyr.goopyutil.entity.cameramap.CameraMappingEntityRenderer;
import net.zephyr.goopyutil.entity.zephyr.ZephyrRenderer;
import net.zephyr.goopyutil.init.*;
import net.zephyr.goopyutil.networking.PayloadDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GoopyUtilClient implements ClientModInitializer {
	public static final String MOD_ID = "goopyutil";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean lookForLists = false;

	@Override
	public void onInitializeClient() {
		ModelLoadingPlugin.register(new ModelLoading());
		ItemInit.clientRegisterItem();
		ComputerInit.init();
		ScreensInit.init();
		BlockInit.registerBlocksOnClient();
		EntityInit.registerEntitiesOnClient();
		try {
			BlackWhitelistInit.Init();
		} catch (IOException e) {
			lookForLists = true;
			LOGGER.info("COULDNT FETCH LISTS");
		}

		PayloadDef.registerS2CPackets();
		HudRenderCallback.EVENT.register(new TabOverlayClass());
		LOGGER.info("Client Initialized.");
	}
}