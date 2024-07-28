package net.zephyr.goopyutil.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.networking.payloads.GetNbtC2SPayload;
import net.zephyr.goopyutil.networking.payloads.SetScreenS2CPayload;

import java.util.HashMap;
import java.util.Map;

public class ScreenUtils {
    public static Map<String, Factory<? extends Screen, ?>> ScreenList = new HashMap<>();
    public static void registerScreen(String id, Factory<? extends Screen, ?> screen){
        ScreenList.put(id, screen);
    }
    public static Map<String, Factory<? extends Screen, ?>> getScreens(){
        return ScreenList;
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, BlockPos blockPos){
        openScreenOnServer(player, screenIndex, blockPos, new NbtCompound());
    }

    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, int entityID){
        openScreenOnServer(player, screenIndex, entityID, new NbtCompound());
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, BlockPos blockPos, NbtCompound data){
        NbtCompound nbt = new NbtCompound();
        nbt.putLong("pos", blockPos.asLong());
        nbt.put("data", data);

        ServerPlayNetworking.send(player, new SetScreenS2CPayload(screenIndex, nbt, PayloadDef.BLOCK_DATA));
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, int entityID, NbtCompound data){
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("entityID", entityID);
        nbt.put("data", data);

        ServerPlayNetworking.send(player, new SetScreenS2CPayload(screenIndex, nbt, PayloadDef.ENTITY_DATA));
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex, NbtCompound data){
        NbtCompound nbt = new NbtCompound();
        nbt.put("data", data);
        ServerPlayNetworking.send(player, new SetScreenS2CPayload(screenIndex, nbt, PayloadDef.ITEM_DATA));
        System.out.println("OPEN");
    }
    public static void openScreenOnServer(ServerPlayerEntity player, String screenIndex){
        ServerPlayNetworking.send(player, new SetScreenS2CPayload(screenIndex, new NbtCompound(), PayloadDef.OTHER_DATA));
    }


    public static void saveNbtFromScreen(NbtCompound data, BlockPos pos){
        NbtCompound nbt = new NbtCompound();
        nbt.put("data", data);
        nbt.putLong("pos", pos.asLong());

        ClientPlayNetworking.send(new GetNbtC2SPayload(nbt, PayloadDef.BLOCK_DATA));
    }

    public static void saveNbtFromScreen(NbtCompound data, int entityID){
        NbtCompound nbt = new NbtCompound();
        nbt.put("data", data);
        nbt.putInt("entityID", entityID);

        ClientPlayNetworking.send(new GetNbtC2SPayload(nbt, PayloadDef.ENTITY_DATA));
    }

    public static void saveNbtFromScreen(NbtCompound data){
        ClientPlayNetworking.send(new GetNbtC2SPayload(data, PayloadDef.ITEM_DATA));
    }

    @FunctionalInterface
    public interface Factory<T extends Screen, R> {
        T create(Text title, NbtCompound data, long other);
    }


    public static void drawResizableText(DrawContext context, TextRenderer textRenderer, Text text, float scale, float x, float y, int color, int backgroundColor, boolean shadow, boolean centered){

        x = x / scale;
        y = y / scale;
        if(centered) x -= (textRenderer.getWidth(text) / 2f);

        MatrixStack matrices = context.getMatrices();
        VertexConsumerProvider verticies = context.getVertexConsumers();

        matrices.push();
        matrices.scale(scale, scale, scale);
        textRenderer.draw(text, x, y, color, shadow, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, backgroundColor, 0xF000F0);
        matrices.pop();
    }
    public static void drawAutoResizedText(DrawContext context, TextRenderer textRenderer, Text text, float baseScale, float maxTextWidth, float x, float y, int color, int backgroundColor, boolean shadow, boolean centered){
        float scale = (textRenderer.getWidth(text) * baseScale) > maxTextWidth ? (baseScale / textRenderer.getWidth(text)) * maxTextWidth : baseScale;
        drawResizableText(context, textRenderer, text, scale, x, y + (1 / scale), color, backgroundColor, shadow, centered);
    }
}
