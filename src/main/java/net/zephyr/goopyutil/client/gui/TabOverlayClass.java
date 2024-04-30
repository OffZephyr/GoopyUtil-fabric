package net.zephyr.goopyutil.client.gui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.zephyr.goopyutil.networking.NetChannels;
import net.zephyr.goopyutil.util.IEntityDataSaver;

public class TabOverlayClass implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {

        MinecraftClient client = MinecraftClient.getInstance();
        World world = null;

        if(client.options.playerListKey.isPressed()) {
            ClientPlayNetworking.send(NetChannels.MONEY_SYNC_CALL, PacketByteBufs.empty());

            float moneyX = 0;
            float y = 0;
            float timeX = 0;
            final float scale = 2f;
            if (client != null) {
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();
                world = client.world;

                moneyX = (width / scale) / 48;
                timeX = ((width / scale) / 48) * 47;
                y = (height / scale) / 24;
            }

            if (world != null) {
                long dayTime = (world.getTimeOfDay());
                double currentDay = dayTime / 24000d;

                long hour = (dayTime / 1000) - ((24000 * (dayTime / 24000)) / 1000);

                boolean isMorning = hour >= 0 && hour < 6;
                boolean isAfternoon = hour >= 12 && hour < 18;
                boolean isNight = hour >= 18 && hour < 24;

                String dayHalf = hour >= 6 && hour < 18 ? " PM" : " AM";
                hour = hour > 6 && hour <= 18 ? hour - 12 : hour > 18 ? hour - 24 : hour;
                String HourDisplay = (6 + hour) + dayHalf;

                String dayPrefix = isNight ? "Night " : isMorning ? "Morning " : isAfternoon ? "Evening " : "Day ";
                String day = dayPrefix + (int) (currentDay + 1);

                NbtCompound data = ((IEntityDataSaver)client.player).getPersistentData();
                int money = data.getInt("Credits");

                MatrixStack matrices = drawContext.getMatrices();
                VertexConsumerProvider verticies = drawContext.getVertexConsumers();

                matrices.push();
                TextRenderer renderer = client.textRenderer;
                matrices.scale(scale, scale, scale);
                renderer.draw("F$: " + money, moneyX, y, 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
                renderer.draw(HourDisplay, timeX - renderer.getWidth(HourDisplay), y, 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
                matrices.scale(1 / scale, 1 / scale, 1 / scale);
                renderer.draw(day, timeX * scale - renderer.getWidth(day), y * scale + (scale * 9), 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
            }
        }
    }
}
