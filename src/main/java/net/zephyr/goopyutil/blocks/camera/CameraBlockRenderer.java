package net.zephyr.goopyutil.blocks.camera;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.camera.CameraBlockEntity;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlock;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockLayer;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlockLayers;
import net.zephyr.goopyutil.client.JavaModels;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.init.ItemInit;

import java.util.Objects;

public class CameraBlockRenderer implements BlockEntityRenderer<CameraBlockEntity> {
    private final ModelPart model;
    private static final String HEAD = "head";
    public CameraBlockRenderer(BlockEntityRendererFactory.Context context){
        ModelPart modelPart = context.getLayerModelPart(JavaModels.CAMERA_HEAD);
        this.model = modelPart.getChild(HEAD);
    }
   public static TexturedModelData getTexturedModelData() {
       ModelData modelData = new ModelData();
       ModelPartData modelPartData = modelData.getRoot();
       modelPartData.addChild(HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -3.0F, -5.1F, 4.0F, 4.0F, 7.0F, new Dilation(0.0F))
               .uv(0, 2).cuboid(-2.0F, -3.0F, -7.1F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
               .uv(10, 11).cuboid(-2.0F, -3.0F, -7.1F, 4.0F, 0.0F, 2.0F, new Dilation(0.0F))
               .uv(0, 0).cuboid(2.0F, -3.0F, -7.1F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 13.0F, 3.0F));
       return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void render(CameraBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {


        BlockPos pos = entity.getPos();

        World world = entity.getWorld();
        boolean bl = world != null;
        BlockState blockState = bl ? entity.getCachedState() : BlockInit.CAMERA.getDefaultState().with(LayeredBlock.FACING, Direction.SOUTH);

        NbtCompound data = entity.getCustomData();
        float f = blockState.get(LayeredBlock.FACING).asRotation();

        matrices.push();
        matrices.translate(0.5f, -0.125f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f + 180));
        String id = "block/camera";
        if(data.getBoolean("isUsed") && data.getBoolean("Active")) id = id + "_on";
        Identifier texture = new Identifier(GoopyUtil.MOD_ID, id);
        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

        float pitch = data.getFloat("pitch");
        float yaw = data.getFloat("yaw");

        if(data.getBoolean("Active")) {
            model.roll = (float) Math.PI / 180 * 180;
            model.pitch = (float) Math.PI / 180 * pitch;
            model.yaw = (float) Math.PI / 180 * yaw;
        }
        else {
            model.roll = (float) Math.PI / 180 * 180;
            model.pitch = (float) Math.PI / 180 * 50;
            model.yaw = (float) Math.PI / 180 * 0;
        }
        model.render(matrices, vertexConsumer, getLightLevel(world, pos), overlay);
        matrices.pop();
    }
    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
