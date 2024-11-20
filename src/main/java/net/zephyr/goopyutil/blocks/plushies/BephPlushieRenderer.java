package net.zephyr.goopyutil.blocks.plushies;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.OffsetRotationBlock;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Environment(EnvType.CLIENT)
public class BephPlushieRenderer implements BlockEntityRenderer<BephPlushieBlockEntity> {
    MinecraftClient client;
    BlockRenderManager manager;

    public BephPlushieRenderer(BlockEntityRendererFactory.Context context){
        client = MinecraftClient.getInstance();
        manager = context.getRenderManager();
    }

    @Override
    public void render(BephPlushieBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockPos pos = entity.getPos();
        BlockState state = client.world.getBlockState(pos);

        if(state.getBlock() instanceof OffsetRotationBlock) {
            matrices.push();
            float rotation = ((IEntityDataSaver)entity).getPersistentData().getFloat("Rotation") + 180;
            float offsetRotation = state.get(OffsetRotationBlock.FACING).asRotation();
            int offsetX = state.get(OffsetRotationBlock.OFFSET_X);
            int offsetZ = state.get(OffsetRotationBlock.OFFSET_Z);

            matrices.translate(-0.5f, 0, -0.5f);
            matrices.translate(offsetX/16f, 0, offsetZ/16f);

            matrices.translate(0.5f, 0, 0.5f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(offsetRotation));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation));
            matrices.translate(-0.5f, 0, -0.5f);
            this.renderModel(pos, state, matrices, vertexConsumers, entity.getWorld(), false, overlay);
            matrices.pop();
        }
    }

    private void renderModel(BlockPos pos, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, boolean cull, int overlay) {
        RenderLayer renderLayer = RenderLayers.getMovingBlockLayer(state);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        this.manager
                .getModelRenderer()
                .render(world, this.manager.getModel(state), state, pos, matrices, vertexConsumer, cull, Random.create(), state.getRenderingSeed(pos), overlay);
    }
}
