package net.zephyr.goopyutil;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.zephyr.goopyutil.init.*;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.util.GoopyBlacklist;
import net.zephyr.goopyutil.util.commands.MoneyCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

public class GoopyUtil implements ModInitializer {
	public static final String MOD_ID = "goopyutil";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ItemInit.registerItems();
		EntityInit.registerEntities();
		BlockEntityInit.registerBlockEntities();
		ItemGroupsInit.registerItemGroups();
		SoundsInit.registerSounds();


		registerCommands();

		PayloadDef.registerC2SPackets();
		LOGGER.info("The GOOP is in the bag.");
	}

	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register(MoneyCommand::register);
	}
}