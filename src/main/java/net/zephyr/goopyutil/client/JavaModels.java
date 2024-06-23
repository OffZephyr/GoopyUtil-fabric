package net.zephyr.goopyutil.client;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;

public class JavaModels {
    public static final EntityModelLayer LAYERED_BLOCK_FACE =
            new EntityModelLayer(Identifier.of(GoopyUtil.MOD_ID, "layered_block_face"), "main");
    public static final EntityModelLayer CAMERA_HEAD =
            new EntityModelLayer(Identifier.of(GoopyUtil.MOD_ID, "camera_head"), "main");
    public static final EntityModelLayer CAMERA_MAP =
            new EntityModelLayer(Identifier.of(GoopyUtil.MOD_ID, "camera_map"), "main");
    public static final EntityModelLayer ZEPHYR =
            new EntityModelLayer(Identifier.of(GoopyUtil.MOD_ID, "zephyr"), "main");
}
