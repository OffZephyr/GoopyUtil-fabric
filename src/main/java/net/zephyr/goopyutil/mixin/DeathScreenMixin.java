package net.zephyr.goopyutil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.nbt.NbtCompound;
import net.zephyr.goopyutil.client.ClientHook;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void customDeathScreen(CallbackInfo ci) {
        /*if(MinecraftClient.getInstance().player.getLastAttacker() instanceof GoopyUtilEntity entity && entity.hasJumpScare()) {
            NbtCompound deathNbt = new NbtCompound();
            deathNbt.putBoolean("isHardcore", MinecraftClient.getInstance().world.getLevelProperties().isHardcore());
            String index = entity.killScreenID;

            ClientHook.openScreen(index, deathNbt, entity.getId());
        }*/
        NbtCompound deathNbt = new NbtCompound();
        deathNbt.putBoolean("isHardcore", MinecraftClient.getInstance().world.getLevelProperties().isHardcore());
        String index = "death_goopy";

        System.out.println(index);

        ClientHook.openScreen(index, deathNbt, 5);
    }
}
