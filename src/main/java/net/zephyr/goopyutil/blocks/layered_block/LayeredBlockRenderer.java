package net.zephyr.goopyutil.blocks.layered_block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.client.JavaModels;
import net.zephyr.goopyutil.init.BlockInit;

import java.util.Objects;

public class LayeredBlockRenderer implements BlockEntityRenderer<LayeredBlockEntity> {
    private final ModelPart model;
    private static final String MODEL = "model";
    BlockPos[] posOffset;
    Integer[][] rotOffset;

    private LayeredBlockLayer[][] Layers = new LayeredBlockLayer[3][6];
    public LayeredBlockRenderer(BlockEntityRendererFactory.Context context){
        ModelPart modelPart = context.getLayerModelPart(JavaModels.LAYERED_BLOCK_FACE);
        this.model = modelPart.getChild(MODEL);
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("model", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, 0.0F));
        return TexturedModelData.of(modelData, 16, 16);
    }

    private void updateLayerData(LayeredBlockEntity entity) {

        for (int j = 0; j < 3; j++) {

            NbtCompound data = entity.getCustomData().getCompound("layer" + j);

            for (int i = 0; i < 6; i++) {

                Layers[j][i] = Objects.equals(data.getString("" + i), "") || data.isEmpty() ?
                        new LayeredBlockLayer("", 0, false, 16) :
                        LayeredBlockLayers.getLayer(data.getString("" + i));
            }

        }
    }

    @Override
    public void render(LayeredBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        BlockPos pos = entity.getPos();

        World world = entity.getWorld();
        boolean bl = world != null;
        BlockState blockState = bl ? entity.getCachedState() : BlockInit.LAYERED_BLOCK_BASE.getDefaultState().with(LayeredBlock.FACING, Direction.SOUTH);
        Block block = blockState.getBlock();
        updateLayerData(entity);

        Direction d = blockState.get(LayeredBlock.FACING);
        float f = blockState.get(LayeredBlock.FACING).asRotation();

        posOffset = new BlockPos[]{
                pos.north(),
                pos.west(),
                pos.south(),
                pos.east(),
                pos.up(),
                pos.down()
        };


        int rotation = entity.getCustomData().getBoolean("rotates") ? (int)-f : 0;
        rotOffset = new Integer[][]{
                { 0, 0, 180},
                { 0, 270, 180},
                { 0, 180, 180},
                { 0, 90, 180},
                { 90, rotation, 0},
                { -90, rotation, 0}
        };

        if (!(block instanceof LayeredBlock)) {

            return;
        }

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                LayeredBlockLayer thisLayer = Layers[i][j];

                if(Objects.equals(thisLayer.getName(), "")){
                    if(i == 0){
                        matrices.push();
                        matrices.translate(0.5f, -0.5f, 0.5f);
                        Identifier texture = new Identifier(GoopyUtil.MOD_ID, "block/layered_block");
                        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture);
                        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

                        render(matrices, vertexConsumer, this.model, posOffset[j], overlay, world, rotOffset[j][0], rotOffset[j][1], rotOffset[j][2], 1, 1, 1);
                        matrices.pop();
                    }
                   continue;
                }
                if(thisLayer.getRgbCount() > 0) {
                    for (int k = 0; k < thisLayer.getRgbCount(); k++) {
                        matrices.push();
                        matrices.translate(0.5f, -0.5f, 0.5f);
                        Identifier texture = thisLayer.getRgbTexture(k);
                        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture);
                        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

                        float r = entity.getCustomData().getCompound("layer" + i).getFloat(j + "_" + k + "_r") / 256f;
                        float g = entity.getCustomData().getCompound("layer" + i).getFloat(j + "_" + k + "_g") / 256f;
                        float b = entity.getCustomData().getCompound("layer" + i).getFloat(j + "_" + k + "_b") / 256f;
                        render(matrices, vertexConsumer, this.model, posOffset[j], overlay, world, rotOffset[j][0], rotOffset[j][1], rotOffset[j][2], r, g, b);
                        matrices.pop();
                    }
                }
                if(thisLayer.hasStaticLayer() || thisLayer.getRgbCount() <= 0){
                    matrices.push();
                    matrices.translate(0.5f, -0.5f, 0.5f);
                    Identifier texture = thisLayer.getTexture();
                    SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture);
                    VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

                    render(matrices, vertexConsumer, this.model, posOffset[j], overlay, world, rotOffset[j][0], rotOffset[j][1], rotOffset[j][2], 1, 1, 1);
                    matrices.pop();
                }
            }
        }
    }
    private void render(MatrixStack matrices, VertexConsumer vertices, ModelPart model, BlockPos checkPos, int overlay, World world, float pitch, float yaw, float roll, float r, float g, float b){
        if(flag(world, checkPos)){
            model.roll = (float) Math.PI / 180 * roll;
            model.pitch = (float) Math.PI / 180 * pitch;
            model.yaw = (float) Math.PI / 180 * yaw;
            model.render(matrices, vertices, getLightLevel(world, checkPos), overlay, r, g, b, 1f);

        }
    }

    boolean flag(World world, BlockPos pos){
        return !world.getBlockState(pos).isOpaqueFullCube(world, pos);
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
    @Override
    public int getRenderDistance() {
        return 256;
    }
}
