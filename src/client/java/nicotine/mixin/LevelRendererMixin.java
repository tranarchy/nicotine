package nicotine.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.phys.Vec3;
import nicotine.events.*;
import nicotine.util.EventBus;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Unique
    Camera camera;
    PoseStack matrixStack;
    MultiBufferSource multiBufferSource;

    @Final
    @Shadow
    private RenderBuffers renderBuffers;

    @Shadow
    @Final
    private LevelTargetBundle targets;

    @Inject(at = @At("HEAD"), method = "extractBlockDestroyAnimation", cancellable = true)
    private void extractBlockDestroyAnimation(final Camera camera, final LevelRenderState levelRenderState, CallbackInfo info) {
        boolean result = EventBus.post(new RenderBlockDamageEvent());

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "addWeatherPass", cancellable = true)
    private void addWeatherPass(FrameGraphBuilder frameGraphBuilder, GpuBufferSlice gpuBufferSlice, CallbackInfo info) {
        boolean result = EventBus.post(new RenderWeatherEvent());

        if (!result) {
            info.cancel();
        }
    }

    @Inject(method = {"renderLevel"}, at = {@At("HEAD")})
    public void renderLevelBefore(
            final GraphicsResourceAllocator resourceAllocator,
            final DeltaTracker deltaTracker,
            final boolean renderOutline,
            final CameraRenderState cameraState,
            final Matrix4fc modelViewMatrix,
            final GpuBufferSlice terrainFog,
            final Vector4f fogColor,
            final boolean shouldRenderSky,
            final ChunkSectionsToRender chunkSectionsToRender, CallbackInfo info) {
        this.camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        this.matrixStack = new PoseStack();
        this.multiBufferSource = this.renderBuffers.bufferSource();

        EventBus.post(new RenderBeforeEvent(camera, matrixStack, multiBufferSource));
    }

    @Inject(method = {"extractLevel"}, at = {@At("HEAD")})
    public void extractLevel(final DeltaTracker deltaTracker, final Camera camera, final float deltaPartialTick, CallbackInfo callbackInfo) {
        EventBus.post(new ExtractLevelEvent());
    }

    @Inject(method = "lambda$addMainPass$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;renderTranslucentFeatures()V", shift = At.Shift.AFTER))
    private void afterRenderTranslucentFeatures(CallbackInfo ci) {
            EventBus.post(new RenderEvent(camera, matrixStack, multiBufferSource));
    }

    @Inject(at = @At("HEAD"), method = "addSkyPass", cancellable = true)
    private void addSkyPass(final FrameGraphBuilder frame, final CameraRenderState cameraState, final GpuBufferSlice skyFog, CallbackInfo info) {
        boolean result = EventBus.post(new RenderSkyEvent());

        if (!result) {
            info.cancel();
        }
    }
}
