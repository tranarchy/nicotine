package nicotine.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.BlockBreakingInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nicotine.events.*;
import nicotine.mod.mods.render.BlockBreaking;
import nicotine.util.EventBus;
import org.joml.Matrix4f;
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
    private void renderBlockDamage(MatrixStack matrices, Camera camera, VertexConsumerProvider.Immediate vertexConsumers, CallbackInfo info) {
        boolean result = EventBus.post(new RenderBlockDamageEvent());

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderWeather", cancellable = true)
    private void renderWeather(FrameGraphBuilder frameGraphBuilder, LightmapTextureManager lightmapTextureManager, Vec3d pos, float tickDelta, Fog fog, CallbackInfo info) {
        boolean result = EventBus.post(new RenderWeatherEvent());

        if (!result) {
            info.cancel();
        }
    }

    @Inject(method = {"render"}, at = {@At("HEAD")})
    private void beforeRender(ObjectAllocator objectAllocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo info) {
        this.camera = camera;
        this.matrixStack = new MatrixStack();
        this.vertexConsumerProvider = this.bufferBuilders.getEntityVertexConsumers();
    }

    /*@Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getCloudRenderModeValue()Lnet/minecraft/client/option/CloudRenderMode;")})
    private void beforeClouds(CallbackInfo info, @Local FrameGraphBuilder frameGraphBuilder) {
        RenderPass afterTranslucentPass = frameGraphBuilder.createPass("afterTranslucent");
        this.framebufferSet.mainFramebuffer = afterTranslucentPass.transfer(this.framebufferSet.mainFramebuffer);
        afterTranslucentPass.setRenderer(() -> {
           EventBus.post(new BeforeCloudsRenderEvent(camera, matrixStack, vertexConsumerProvider));
        });
    }*/

    @Inject(
            method = "method_62214",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/debug/DebugRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/Frustum;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDD)V",
                    ordinal = 0
            )
    )
    private void beforeDebugRender(CallbackInfo ci) {
        EventBus.post(new RenderEvent(camera, matrixStack, vertexConsumerProvider));
    }

    @Inject(method = "method_62214", at = @At(value = "CONSTANT", args = "stringValue=blockentities", ordinal = 0))
    private void afterEntities(CallbackInfo ci) {
        EventBus.post(new AfterEntitiesRenderEvent(camera, matrixStack, vertexConsumerProvider));
    }

    @Inject(at = @At("HEAD"), method = "renderParticles", cancellable = true)
    private void renderParticles(FrameGraphBuilder frameGraphBuilder, Camera camera, LightmapTextureManager lightmapTextureManager, float tickDelta, Fog fog, CallbackInfo info) {
        boolean result = EventBus.post(new RenderParticlesEvent());

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderEntity")
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info) {
        if (vertexConsumers instanceof OutlineVertexConsumerProvider outlineVertexConsumerProvider) {
            EventBus.post(new RenderEntityOutlineEvent(outlineVertexConsumerProvider));
        }
    }
}
