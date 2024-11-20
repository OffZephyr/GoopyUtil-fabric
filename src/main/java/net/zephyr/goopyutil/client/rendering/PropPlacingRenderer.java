package net.zephyr.goopyutil.client.rendering;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.zephyr.goopyutil.blocks.OffsetRotationBlock;
import org.joml.Matrix4f;

import java.util.List;

public class PropPlacingRenderer {
        public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof OffsetRotationBlock block) {
                    OffsetRotationBlock.drawingOutline = true;
                    HitResult blockHit = client.crosshairTarget;
                    if (blockHit.getType() == HitResult.Type.BLOCK) {
                        BlockPos pos = ((BlockHitResult) blockHit).getBlockPos();
                        Vec3d hitPos = blockHit.getPos().add(0, 1, 0);
                        if (((BlockHitResult) blockHit).getSide() == Direction.UP) pos = pos.up();
                        if (((BlockHitResult) blockHit).getSide() == Direction.DOWN) pos = pos.down();

                        float rotation = -player.getHeadYaw();

                        double x = hitPos.getX() - pos.getX();
                        double y = pos.getY();
                        double z = hitPos.getZ() - pos.getZ();

                        x = Math.clamp(x, 0, 1);
                        z = Math.clamp(z, 0, 1);

                        if(player.isSneaking()){
                            x = 0.5f;
                            z = 0.5f;
                            pos = pos.offset(((BlockHitResult) blockHit).getSide());

                            rotation = Math.round(rotation / OffsetRotationBlock.angleSnap) * OffsetRotationBlock.angleSnap;
                        }
                        else if(!client.world.getBlockState(pos).isOf(Blocks.AIR)){
                            if (((BlockHitResult) blockHit).getSide() == Direction.NORTH) z = 0;
                            if (((BlockHitResult) blockHit).getSide() == Direction.EAST) x = 1;
                            if (((BlockHitResult) blockHit).getSide() == Direction.SOUTH) z = 1;
                            if (((BlockHitResult) blockHit).getSide() == Direction.WEST) x = 0;
                        }

                        BlockState state = block.getDefaultState()
                                .with(OffsetRotationBlock.OFFSET_X, (int) (x * OffsetRotationBlock.offset_grid_size))
                                .with(OffsetRotationBlock.OFFSET_Z, (int) (z * OffsetRotationBlock.offset_grid_size));

                        VoxelShape shape = state.getOutlineShape(client.world, pos, ShapeContext.absent());

                        matrices.push();

                        matrices.translate(-0.5f, 0, -0.5f);
                        matrices.translate(x + pos.getX() - 0.5f, y, z + pos.getZ() - 0.5f);

                        matrices.translate(0.5f, 0, 0.5f);
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
                        matrices.translate(-0.5f, 0, -0.5f);
                        BakedModel model = client.getBakedModelManager().getBlockModels().getModel(state);

                        client.getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), state, model, 1, 1, 1, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);

                        WorldRenderer.drawShapeOutline(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), shape, 0, 0, 0, 1,1 ,1, 0.5f, true);
                        for (Box box : shape.getBoundingBoxes()) {
                            DebugRenderer.drawBox(matrices, vertexConsumers, box, 2, 2, 2, 0.05f);
                        }
                        matrices.pop();
                        OffsetRotationBlock.drawingOutline = false;
                    }
                }
            }
        }
}
