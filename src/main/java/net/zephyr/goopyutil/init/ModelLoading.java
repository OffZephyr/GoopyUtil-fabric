package net.zephyr.goopyutil.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockLayer;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockLayers;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockModel;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ModelLoading implements ModelLoadingPlugin {
    private static final Identifier LAYERED_BLOCK_ID = Identifier.of(GoopyUtil.MOD_ID, "layered_block");
    public static final ModelIdentifier BLOCK_MODEL_NORTH = new ModelIdentifier(LAYERED_BLOCK_ID, "facing=north");
    public static final ModelIdentifier BLOCK_MODEL_EAST = new ModelIdentifier(LAYERED_BLOCK_ID, "facing=east");
    public static final ModelIdentifier BLOCK_MODEL_SOUTH = new ModelIdentifier(LAYERED_BLOCK_ID, "facing=south");
    public static final ModelIdentifier BLOCK_MODEL_WEST = new ModelIdentifier(LAYERED_BLOCK_ID, "facing=west");
    public static final ModelIdentifier BLOCK_MODEL_INVENTORY = new ModelIdentifier(LAYERED_BLOCK_ID, "inventory");
    public static final Identifier BASE_LAYERED_TEXTURE = Identifier.of(GoopyUtil.MOD_ID, "block/layered_block");

    @Override
    public void onInitializeModelLoader(Context pluginContext) {

        pluginContext.modifyModelOnLoad().register((original, context) -> {
            if (context.topLevelId() != null && ((context.topLevelId().equals(BLOCK_MODEL_NORTH)) || (context.topLevelId().equals(BLOCK_MODEL_EAST)) || (context.topLevelId().equals(BLOCK_MODEL_SOUTH)) || (context.topLevelId().equals(BLOCK_MODEL_WEST)) || (context.topLevelId().equals(BLOCK_MODEL_INVENTORY)))) {
                return new LayeredBlockModel();
            } else {
                return original;
            }
        });
    }
}
