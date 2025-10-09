package nicotine.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import net.minecraft.client.render.*;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.BlockBreakingInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nicotine.events.*;
import nicotine.mod.mods.render.BlockBreaking;
import nicotine.util.EventBus;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Unique
    Camera camera;
    MatrixStack matrixStack;
    VertexConsumerProvider vertexConsumerProvider;

    @Final
    @Shadow
    private Int2ObjectMap<BlockBreakingInfo> blockBreakingInfos;

    @Final
    @Shadow
    private BufferBuilderStorage bufferBuilders;

    @Shadow
    @Final
    private DefaultFramebufferSet framebufferSet;

    @Inject(at = @At("HEAD"), method = "setBlockBreakingInfo")
    public void setBlockBreakingInfo(int entityId, BlockPos pos, int stage, CallbackInfo info) {
        BlockBreakingInfo blockBreakingInfo = this.blockBreakingInfos.get(entityId);

        if (stage >= 0 && stage < 10 && blockBreakingInfo != null) {
            BlockBreaking.blockBreakingInfos.put(blockBreakingInfo.getPos(), blockBreakingInfo.getStage());
        } else if (blockBreakingInfo != null) {
            BlockBreaking.blockBreakingInfos.remove(blockBreakingInfo.getPos());
        }
    }

    @Inject(at = @At("HEAD"), method = "renderBlockDamage", cancellable = true)
    private void renderBlockDamage(MatrixStack matrices, VertexConsumerProvider.Immediate immediate, WorldRenderState renderStates, CallbackInfo info) {
        boolean result = EventBus.post(new RenderBlockDamageEvent());

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderWeather", cancellable = true)
    private void renderWeather(FrameGraphBuilder frameGraphBuilder, Vec3d cameraPos, GpuBufferSlice fogBuffer, CallbackInfo info) {
        boolean result = EventBus.post(new RenderWeatherEvent());

        if (!result) {
            info.cancel();
        }
    }

    @Inject(method = {"render"}, at = {@At("HEAD")})
    public void render(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f matrix4f, Matrix4f projectionMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky, CallbackInfo info) {
        this.camera = camera;
        this.matrixStack = new MatrixStack();
        this.vertexConsumerProvider = this.bufferBuilders.getEntityVertexConsumers();

        EventBus.post(new RenderEntityOutlineEvent(this.bufferBuilders.getOutlineVertexConsumers()));
    }

    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getCloudRenderModeValue()Lnet/minecraft/client/option/CloudRenderMode;", shift = At.Shift.BEFORE)})
    private void beforeCloudsX(CallbackInfo info, @Local FrameGraphBuilder frameGraphBuilder) {
        FramePass afterTranslucentPass = frameGraphBuilder.createPass("afterTranslucent");
        this.framebufferSet.mainFramebuffer = afterTranslucentPass.transfer(this.framebufferSet.mainFramebuffer);
        afterTranslucentPass.setRenderer(() -> {
            EventBus.post(new RenderEvent(camera, matrixStack, vertexConsumerProvider));
        });
    }

    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getCloudRenderModeValue()Lnet/minecraft/client/option/CloudRenderMode;", shift = At.Shift.AFTER)})
    private void beforeCloudsY(CallbackInfo info, @Local FrameGraphBuilder frameGraphBuilder) {
        FramePass afterTranslucentPass = frameGraphBuilder.createPass("afterTranslucent");
        this.framebufferSet.mainFramebuffer = afterTranslucentPass.transfer(this.framebufferSet.mainFramebuffer);
        afterTranslucentPass.setRenderer(() -> {
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
            GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

            EventBus.post(new RenderAfterEvent(camera, matrixStack, vertexConsumerProvider));

            GL11.glEnable(GL11.GL_DEPTH_TEST);

            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        });
    }

    @Inject(at = @At("HEAD"), method = "renderParticles", cancellable = true)
    private void renderParticles(FrameGraphBuilder frameGraphBuilder, GpuBufferSlice fogBuffer, CallbackInfo info) {
        boolean result = EventBus.post(new RenderParticlesEvent());

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderSky", cancellable = true)
    private void renderSky(FrameGraphBuilder frameGraphBuilder, Camera camera, GpuBufferSlice fogBuffer, CallbackInfo info) {
        boolean result = EventBus.post(new RenderSkyEvent());

        if (!result) {
            info.cancel();
        }
    }
}
