package nicotine.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import nicotine.events.*;
import nicotine.util.EventBus;
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

    @Inject(method = {"render"}, at = {@At("HEAD")})
    public void render(final GraphicsResourceAllocator resourceAllocator, final boolean renderOutline, final CameraRenderState cameraState, final GpuBufferSlice terrainFog, final Vector4f fogColor, final boolean shouldRenderSky, final boolean consistentDepthRequired, CallbackInfo info) {
        this.camera = Minecraft.getInstance().gameRenderer.mainCamera();
        this.matrixStack = new PoseStack();

        EventBus.post(new RenderBeforeEvent(camera, matrixStack, submitNodeStorage));
    }

    @WrapOperation(method = "executeClassicTransparency", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;renderGroup(Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;Lcom/mojang/renderpearl/api/commands/RenderPass;Lcom/mojang/renderpearl/api/textures/GpuSampler;Lcom/mojang/renderpearl/api/textures/GpuTextureView;Z)V"))
    private void wrapRenderTranslucentTerrain(ChunkSectionsToRender chunkSectionsToRender, ChunkSectionLayerGroup group, RenderPass renderPass, GpuSampler sampler, GpuTextureView atlas, boolean renderWireframeTerrain, Operation<Void> original) {
        original.call(chunkSectionsToRender, group, renderPass, sampler, atlas, renderWireframeTerrain);
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
