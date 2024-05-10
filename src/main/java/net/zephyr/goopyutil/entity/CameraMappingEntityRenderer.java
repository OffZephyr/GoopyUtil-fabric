package net.zephyr.goopyutil.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.client.JavaModels;
import net.zephyr.goopyutil.init.ItemInit;

import java.util.Objects;

public class CameraMappingEntityRenderer extends EntityRenderer<CameraMappingEntity> {
    private final ModelPart side;
    private final ModelPart end;
    private static final String BASE = "Base";
    private static final String SIDE = "Side";
    private static final String END = "End";
    public CameraMappingEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        ModelPart modelPart = ctx.getPart(JavaModels.CAMERA_MAP).getChild(BASE);
        this.side = modelPart.getChild(SIDE);
        this.end = modelPart.getChild(END);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData Base = modelPartData.addChild("Base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        Base.addChild("Side", ModelPartBuilder.create()
                .uv(0, 0).mirrored().cuboid(7.0F, 7.0F, -8.0F, 2.0F, 2.0F, 16.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 0).mirrored().cuboid(-9.0F, 7.0F, -8.0F, 2.0F, 2.0F, 16.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 0).mirrored().cuboid(7.0F, -9.0F, -8.0F, 2.0F, 2.0F, 16.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 0).mirrored().cuboid(-9.0F, -9.0F, -8.0F, 2.0F, 2.0F, 16.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

        Base.addChild("End", ModelPartBuilder.create()
                .uv(0, 0).mirrored().cuboid(-7.0F, 7.0F, -9.0F, 14.0F, 2.0F, 2.0F, new Dilation(0.001F)).mirrored(false)
                .uv(0, 0).mirrored().cuboid(-7.0F, -9.0F, -9.0F, 14.0F, 2.0F, 2.0F, new Dilation(0.001F)).mirrored(false)
                .uv(0, 0).mirrored().cuboid(-9.0F, -9.0F, -9.0F, 2.0F, 18.0F, 2.0F, new Dilation(0.001F)).mirrored(false)
                .uv(0, 0).mirrored().cuboid(7.0F, -9.0F, -9.0F, 2.0F, 18.0F, 2.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.pivot(0.0F, -8.0F, 0.0F));
        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public void render(CameraMappingEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        if(entity.getId() == MinecraftClient.getInstance().player.getMainHandStack().getOrCreateNbt().getInt("mapEntityID") && MinecraftClient.getInstance().player.getOffHandStack().isOf(ItemInit.TABLET)){
            NbtCompound data = MinecraftClient.getInstance().player.getMainHandStack().getOrCreateNbt();
            NbtCompound tabletData = MinecraftClient.getInstance().player.getOffHandStack().getOrCreateNbt();
            if(data.getBoolean("hasCorner")){
                BlockPos pos1 = BlockPos.fromLong(data.getLong("setupCorner1"));
                BlockPos pos2 = BlockPos.fromLong(data.getLong("setupCorner2"));

                renderLine(entity, matrices, vertexConsumers, pos1, pos2, 0.75f, 1.0f, 0.5f, 0.5f, true);
            }

            if(!tabletData.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty()){
                NbtList mapNbt = tabletData.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();

                HitResult blockHit = MinecraftClient.getInstance().player.raycast(20.0, 0.0f, false);
                BlockPos pos = ((BlockHitResult)blockHit).getBlockPos();

                for(int i = 0; i < mapNbt.size(); i++){
                    if(mapNbt.getLongArray(i).length > 0){
                        BlockPos pos1 = BlockPos.fromLong(mapNbt.getLongArray(i)[0]);
                        BlockPos pos2 = BlockPos.fromLong(mapNbt.getLongArray(i)[1]);
                        Box line = new Box(pos1, pos2).expand(0.05f);
                        if(!data.getBoolean("hasCorner") && line.contains(pos.getX(), pos.getY(), pos.getZ())){
                            if(MinecraftClient.getInstance().player.isSneaking()){
                                renderLine(entity, matrices, vertexConsumers, pos1, pos2, 1.0f, 0.5f, 0.5f, 0.5f, true);
                            }
                            else {
                                renderLine(entity, matrices, vertexConsumers, pos1, pos2, 0.5f, 0.75f, 1.0f, 0.75f, true);
                            }
                        }
                        else {
                            renderLine(entity, matrices, vertexConsumers, pos1, pos2, 1.0f, 1.0f, 1.0f, 0.5f, false);
                        }
                    }
                }
            }


        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(CameraMappingEntity entity) {
        return null;
    }

    void renderLine(CameraMappingEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockPos pos1, BlockPos pos2, float red, float green, float blue, float alpha, boolean selected){
        Identifier texture = new Identifier(GoopyUtil.MOD_ID, "block/white");
        int rot = 0;
        boolean bl = pos1.getX() != pos2.getX();
        boolean bl2 = pos1.getX() > pos2.getX();
        boolean bl3 = pos1.getZ() > pos2.getZ();
        if(bl){
            if(bl2){
                rot = 270;
            }
            else {
                rot = 90;
            }
        }
        else {
            if(bl3) rot = 180;
        }

        int xDist = Math.abs(pos2.getX() - pos1.getX());
        int zDist = Math.abs(pos2.getZ() - pos1.getZ());
        int distance =  1 + Math.max(xDist, zDist);

        float xOff = (pos2.getX() - pos1.getX()) / 2f;
        float zOff = (pos2.getZ() - pos1.getZ()) / 2f;

        float y = 1 + (pos1.getY() - entity.getBlockPos().getY());

        float xTrans = pos1.getX() - entity.getBlockPos().getX();
        float zTrans = pos1.getZ() - entity.getBlockPos().getZ();
        float xStart = pos1.getX() - entity.getBlockPos().getX();
        float zStart = pos1.getZ() - entity.getBlockPos().getZ();
        float xEnd = pos2.getX() - entity.getBlockPos().getX();
        float zEnd = pos2.getZ() - entity.getBlockPos().getZ();

        if(bl){
            xTrans += xOff;
        }
        else {
            zTrans += zOff;
        }

        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
        matrices.push();
        matrices.translate(xTrans, y, zTrans);

        if(selected) matrices.scale(1.0015f, 1.0015f, 1.0015f);

        if(!bl) matrices.scale(1, 1, distance);
        else matrices.scale(distance, 1, 1);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rot));
        side.render(matrices, vertexConsumer, 255, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rot));

        if(!bl) matrices.scale(1, 1, 1f/distance);
        else matrices.scale(1f/distance, 1, 1);

        matrices.translate(-xTrans, 0, -zTrans);
        matrices.translate(xStart, 0, zStart);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rot));
        end.render(matrices, vertexConsumer, 255, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rot));

        matrices.translate(-xStart, 0, -zStart);
        matrices.translate(xEnd, 0, zEnd);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rot + 180));
        end.render(matrices, vertexConsumer, 255, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
        matrices.pop();
    }
}
