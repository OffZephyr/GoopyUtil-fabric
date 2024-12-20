package net.zephyr.goopyutil.blocks.camera;

import net.minecraft.block.BlockState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.camera_desk.CameraDeskBlockEntity;
import net.zephyr.goopyutil.blocks.camera_desk.CameraRenderer;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlock;
import net.zephyr.goopyutil.client.JavaModels;
import net.zephyr.goopyutil.init.BlockInit;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;

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

        for (BlockPos pos : CameraDeskBlockEntity.posList) {
            if (entity.getWorld().getBlockEntity(pos) instanceof CameraDeskBlockEntity mirror) {
                if(CameraRenderer.isDrawing() && BlockPos.fromLong(mirror.currentCam).equals(entity.getPos())) return;
            }
        }

        BlockPos pos = entity.getPos();

        World world = entity.getWorld();
        boolean bl = world != null;
        BlockState blockState = bl ? entity.getCachedState() : BlockInit.CAMERA.getDefaultState().with(LayeredBlock.FACING, Direction.SOUTH);

        NbtCompound data = ((IEntityDataSaver)entity).getPersistentData();
        float f = blockState.get(LayeredBlock.FACING).asRotation();

        matrices.push();
        matrices.translate(0.5f, -0.125f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f + 180));
        String id = "block/camera";
        Identifier texture = Identifier.of(GoopyUtil.MOD_ID, id);
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

        if(data.getBoolean("isUsed") && data.getBoolean("Active")){
            String on_id = "block/camera_on";

            Identifier textureOn = Identifier.of(GoopyUtil.MOD_ID, on_id);
            SpriteIdentifier spriteIdentifier2 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, textureOn);
            VertexConsumer vertexConsumer2 = spriteIdentifier2.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

            model.render(matrices, vertexConsumer2, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);
        }
        matrices.pop();
    }
    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
