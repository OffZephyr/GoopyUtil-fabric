package net.zephyr.goopyutil.blocks.camera_desk;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.zephyr.goopyutil.GoopyUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;
import org.ladysnake.satin.api.managed.ManagedCoreShader;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
public class CameraRenderer {
    public static final ManagedCoreShader SHADER = ShaderEffectManager.getInstance().manageCoreShader(Identifier.of(GoopyUtil.MOD_ID, "warp"), VertexFormats.POSITION_TEXTURE);
        private static final Framebuffer[] framebuffers = new Framebuffer[3];
        private static int Deep;

        public static boolean isDrawing() {
            return Deep > 0;
        }
        public static boolean canDraw() {
            boolean fabulous = MinecraftClient.getInstance().options.getGraphicsMode().getValue() == GraphicsMode.FABULOUS;
            return !fabulous && Deep < 4;
        }

        @Nullable
        public static Framebuffer getFramebuffer() {
            if (!canDraw()) return null;
            return framebuffers[Deep - 1];
        }

        public static void onResize(int width, int height) {
            for (int i = 0; i < framebuffers.length; i++) {
                framebuffers[i] = new SimpleFramebuffer(width, height, true, false);
            }
        }

        public static void onRenderWorld(WorldRenderContext ctx) {
            for (BlockPos pos : CameraDeskBlockEntity.posList) {
                var blockentity = ctx.world().getBlockEntity(pos);
                if (blockentity instanceof CameraDeskBlockEntity mirror) {
                    var cameraPos = ctx.camera().getPos();
                    var entityPos = mirror.getPos();
                    float cameraYaw = ctx.camera().getYaw();
                    float entityYaw = mirror.getYaw();
                    float epsilon = 180;
                    boolean withinDistance = entityPos.toCenterPos().distanceTo(cameraPos) < 128.0;
                    boolean withinYawRange = Math.abs(entityYaw - cameraYaw) % 360 < 180 + epsilon && Math.abs(entityYaw - cameraYaw) % 360 > 180 - epsilon;

                    if (withinDistance && withinYawRange) {
                        renderMirror(mirror, ctx.matrixStack(), ctx.tickCounter().getTickDelta(false));
                    }
                }
            }
        }


        private static void renderMirror(CameraDeskBlockEntity entity, MatrixStack matrices, float tickDelta) {
            int tex = renderWorld(entity);
            if (tex == -1) {
                tex = MinecraftClient.getInstance().getTextureManager().getTexture(Identifier.of(GoopyUtil.MOD_ID, "textures/block/layered_block.png")).getGlId();
            }


            RenderSystem.setShader(SHADER::getProgram);
            RenderSystem.getShader().getUniform("time").set((float)MinecraftClient.getInstance().world.getTime() + tickDelta);
            RenderSystem.getShader().getUniform("scanLineIntensity").set(0.05f);
            RenderSystem.setShaderTexture(0, tex);
            RenderSystem.enableDepthTest();


            var camera = MinecraftClient.getInstance().gameRenderer.getCamera();
             var entityPos = entity.getPos().toCenterPos();
            var cameraPos = camera.getPos();
            var translation = entityPos.subtract(cameraPos);

            matrices.push();
            matrices.translate(translation.x, translation.y, translation.z);
            matrices.multiply(entity.getWorld().getBlockState(entity.getPos()).get(CameraDeskBlock.FACING).getRotationQuaternion());

            matrices.translate(0, 0.5, 0);



            var buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

            float tWidth = 0.4f;
            float tHeight = 0.4f;
            float vWidth = 0.5f;
            float vHeight = 0.5f;

            buffer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, -vHeight).texture(0.5f + tWidth, 0.5f + tHeight);
            buffer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, vHeight).texture(0.5f + tWidth, 0.5f - tHeight);
            buffer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, vHeight).texture(0.5f - tWidth, 0.5f - tHeight);
            buffer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, -vHeight).texture(0.5f - tWidth, 0.5f + tHeight);

            BufferRenderer.drawWithGlobalProgram(buffer.end());

            matrices.pop();
        }

        private static int renderWorld(CameraDeskBlockEntity entity) {
            if (!canDraw()) return -1;

            var client = MinecraftClient.getInstance();
            var camera = client.gameRenderer.getCamera();
            var position = entity.getPos().toCenterPos();
            var direction = entity.getWorld().getBlockState(entity.getPos()).get(CameraDeskBlock.FACING);

            try {

                var oldModelViewStack = RenderSystem.getModelViewStack();
                var oldModelViewMat = new Matrix4f(RenderSystem.getModelViewMatrix());
                var prevProjMat = new Matrix4f(RenderSystem.getProjectionMatrix());
                var oldFrustum = client.worldRenderer.frustum;
                var oldPos = camera.pos;
                float oldYaw = camera.getYaw();
                float oldPitch = camera.getPitch();
                int oldFboWidth = client.getWindow().getFramebufferWidth();
                int oldFboHeight = client.getWindow().getFramebufferHeight();

                // TODO angle is not correct

                // Set camera position and rotation
                camera.pos = new Vec3d(position.toVector3f());

                camera.setRotation(direction.asRotation(), 0);

                var cameraRotation = camera.getRotation();

                // Set up frustum
                var rotMat = new Matrix4f().rotate(cameraRotation.conjugate(new Quaternionf()));
                var projMat = client.gameRenderer.getBasicProjectionMatrix(70f);
                client.worldRenderer.setupFrustum(camera.getPos(), rotMat, projMat);
                RenderSystem.viewport(0, 0, MinecraftClient.getInstance().getFramebuffer().textureWidth, MinecraftClient.getInstance().getFramebuffer().textureHeight);

                Deep++;

                var framebuffer = getFramebuffer();
                if (framebuffer == null) {
                    Deep--;
                    return -1;
                }
                framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);

                framebuffer.beginWrite(true);
                client.getWindow().setFramebufferWidth(framebuffer.textureWidth);
                client.getWindow().setFramebufferHeight(framebuffer.textureHeight);
                client.gameRenderer.loadProjectionMatrix(projMat);
                RenderSystem.modelViewStack = new Matrix4fStack(16);
                client.worldRenderer.render(client.getRenderTickCounter(), false, camera, client.gameRenderer, client.gameRenderer.getLightmapTextureManager(), rotMat, projMat);
                Deep--;

                // Restore original values
                client.getFramebuffer().beginWrite(false);
                camera.pos = oldPos;
                camera.setRotation(oldYaw, oldPitch);
                client.worldRenderer.frustum = oldFrustum;
                client.gameRenderer.loadProjectionMatrix(prevProjMat);
                RenderSystem.modelViewStack = oldModelViewStack;
                RenderSystem.modelViewMatrix = oldModelViewMat;
                client.getWindow().setFramebufferWidth(oldFboWidth);
                client.getWindow().setFramebufferHeight(oldFboHeight);
                RenderSystem.viewport(0, 0, client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());

                return framebuffer.getColorAttachment();
            } catch (Exception ex) {
                ex.printStackTrace();
                return -1;
            }
        }
    }

