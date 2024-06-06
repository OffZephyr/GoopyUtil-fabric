package net.zephyr.goopyutil;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.zephyr.goopyutil.entity.CameraMappingEntity;
import net.zephyr.goopyutil.init.*;
import net.zephyr.goopyutil.networking.NetChannels;
import net.zephyr.goopyutil.util.commands.MoneyCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GoopyUtil implements ModInitializer {
	public static final String MOD_ID = "goopyutil";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ItemInit.registerItems();
		EntityInit.registerEntities();
		BlockInit.registerBlocks();
		BlockEntityInit.registerBlockEntities();
		ItemGroupsInit.registerItemGroups();

		registerCommands();

		NetChannels.registerC2SPackets();

		LOGGER.info("The GOOP is in the bag.");
	}

	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register(MoneyCommand::register);
	}
}