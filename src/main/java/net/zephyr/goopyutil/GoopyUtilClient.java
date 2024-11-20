package net.zephyr.goopyutil;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.zephyr.goopyutil.blocks.camera_desk.CameraDeskBlockRenderer;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.client.gui.TabOverlayClass;
import net.zephyr.goopyutil.init.*;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.util.KeyInputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GoopyUtilClient implements ClientModInitializer {
	public static final String MOD_ID = "goopyutil";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean lookForLists = false;

	@Override
	public void onInitializeClient() {
		ComputerData.addInitializer(new DefaultComputerInit());

		ModelLoadingPlugin.register(new ModelLoading());
		ItemInit.clientRegisterItem();
		ComputerData.runInitializersClient();
		ScreensInit.init();
		BlockInit.registerBlocksOnClient();
		EntityInit.registerEntitiesOnClient();

		KeyInputHandler.register();

		try {
			BlackWhitelistInit.Init();
		} catch (IOException e) {
			lookForLists = true;
			LOGGER.info("COULDNT FETCH LISTS");
		}

		NetworkingInit.registerClientReceivers();
		PayloadDef.registerS2CPackets();
		HudRenderCallback.EVENT.register(new TabOverlayClass());
		LOGGER.info("Client Initialized.");
	}
}