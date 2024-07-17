package net.zephyr.goopyutil.blocks.camera_desk;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.client.JavaModels;
import org.joml.Matrix4f;

public class CameraDeskBlockRenderer  implements BlockEntityRenderer<CameraDeskBlockEntity> {
    MinecraftClient client;
    Framebuffer framebuffer;
    RenderLayer renderLayer;
    NativeImageBackedTexture texture;
    ModelData modelData = new ModelData();
    private static final String SCREEN = "screen";
    ModelPartData modelPartData = modelData.getRoot();
    private final ModelPart model;
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(SCREEN, ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 32, 32);
    }
    public CameraDeskBlockRenderer(BlockEntityRendererFactory.Context context){
        client = MinecraftClient.getInstance();
        //framebuffer = new WindowFramebuffer(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
        framebuffer = client.getFramebuffer();
        ModelPart modelPart = context.getLayerModelPart(JavaModels.CAMERA_SCREEN);
        this.model = modelPart.getChild(SCREEN);

    }
    @Override
    public void render(CameraDeskBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        this.texture = new NativeImageBackedTexture(128, 128, true);

        NativeImage nativeImage = ScreenshotRecorder.takeScreenshot(framebuffer);
        this.texture.setImage(nativeImage);

        Identifier identifier = this.client.getTextureManager().registerDynamicTexture("MONITOR" +  entity.getPos().asLong(), this.texture);
        this.renderLayer = RenderLayer.getText(identifier);

        String id = "block/camera";
        Identifier texture = Identifier.of(GoopyUtil.MOD_ID, id);
        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture);
        VertexConsumer vertices = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

        //VertexConsumer vertices = vertexConsumers.getBuffer(renderLayer);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        matrices.push();
        vertices.vertex(matrix, 0.0f, 128.0f, -0.01f).color(Colors.WHITE).texture(0.0f, 1.0f).light(light);
        vertices.vertex(matrix, 128.0f, 128.0f, -0.01f).color(Colors.WHITE).texture(1.0f, 1.0f).light(light);
        vertices.vertex(matrix, 128.0f, 0.0f, -0.01f).color(Colors.WHITE).texture(1.0f, 0.0f).light(light);
        vertices.vertex(matrix, 0.0f, 0.0f, -0.01f).color(Colors.WHITE).texture(0.0f, 0.0f).light(light);
        model.render(matrices, vertices, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay);
        matrices.pop();
    }
}
