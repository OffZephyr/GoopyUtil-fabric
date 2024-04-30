package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.client.gui.screens.BlacklistScreen;
import net.zephyr.goopyutil.util.GoopyBlacklist;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;

@Mixin(MinecraftClient.class)
public class BlacklistMixin {
	@Inject(at = @At("HEAD"), method = "onInitFinished", cancellable = true)
	private void init(CallbackInfo info) {
		String UUID = MinecraftClient.getInstance().getSession().getUuid();
		String Username = MinecraftClient.getInstance().getSession().getUsername();
		boolean BlacklistedName = GoopyBlacklist.getBlacklist().containsKey(Username);
		boolean BlacklistedUUID = GoopyBlacklist.getBlacklist().containsValue(UUID);
		boolean WhitelistedName = GoopyBlacklist.getWhitelist().containsKey(Username);
		boolean WhitelistedUUID = GoopyBlacklist.getWhitelist().containsValue(UUID);
		if((BlacklistedName || BlacklistedUUID) && !(WhitelistedName || WhitelistedUUID)){

			GoopyUtil.LOGGER.info("UH OH! Seems like you were Blacklisted.");
			MinecraftClient.getInstance().setScreen(new BlacklistScreen());

			info.cancel();
		}
	}
}