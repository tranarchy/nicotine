package nicotine.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
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

    @Final
    @Shadow
    private SubmitNodeStorage submitNodeStorage;

    @Inject(at = @At("HEAD"), method = "submitBlockDestroyAnimation", cancellable = true)
    private void submitBlockDestroyAnimation(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final LevelRenderState levelRenderState, CallbackInfo info) {
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

    @Inject(method = {"render"}, at = {@At("HEAD")})
    public void render(final GraphicsResourceAllocator resourceAllocator, final DeltaTracker deltaTracker, final boolean renderOutline, final CameraRenderState cameraState, final Matrix4fc modelViewMatrix, final GpuBufferSlice terrainFog, final Vector4f fogColor, final boolean shouldRenderSky, CallbackInfo info) {
        this.camera = Minecraft.getInstance().gameRenderer.mainCamera();
        this.matrixStack = new PoseStack();

        EventBus.post(new RenderBeforeEvent(camera, matrixStack, submitNodeStorage));
    }

    @Inject(method = "lambda$addMainPass$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher$PreparedFrame;executeTranslucent()V", shift = At.Shift.AFTER))
    private void afterRenderTranslucentFeatures(CallbackInfo ci) {
            EventBus.post(new RenderEvent(camera, matrixStack, submitNodeStorage));
    }

    @Inject(at = @At("HEAD"), method = "addSkyPass", cancellable = true)
    private void addSkyPass(final FrameGraphBuilder frame, final CameraRenderState cameraState, final GpuBufferSlice skyFog, CallbackInfo info) {
        boolean result = EventBus.post(new RenderSkyEvent());

        if (!result) {
            info.cancel();
        }
    }
}
