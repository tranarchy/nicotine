package nicotine.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;
import nicotine.events.RenderEvent;
import nicotine.events.RenderParticlesEvent;
import nicotine.events.RenderWeatherEvent;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import nicotine.util.EventBus;
import nicotine.util.Render;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Unique
    Camera camera;
    MatrixStack matrixStack;
    VertexConsumerProvider vertexConsumerProvider;

    @Final
    @Shadow
    private BufferBuilderStorage bufferBuilders;

    @Shadow
    @Final
    private DefaultFramebufferSet framebufferSet;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/render/WorldRenderer;renderWeather(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Vec3d;FLnet/minecraft/client/render/Fog;)V", cancellable = true)
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

    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getCloudRenderModeValue()Lnet/minecraft/client/option/CloudRenderMode;")})
    private void beforeClouds(CallbackInfo info, @Local FrameGraphBuilder frameGraphBuilder) {
        RenderPass afterTranslucentPass = frameGraphBuilder.createPass("afterTranslucent");
        this.framebufferSet.mainFramebuffer = afterTranslucentPass.transfer(this.framebufferSet.mainFramebuffer);
        afterTranslucentPass.setRenderer(() -> {
           EventBus.post(new RenderEvent(camera, matrixStack, vertexConsumerProvider));
        });
    }

    @Inject(at = @At("HEAD"), method = "renderParticles", cancellable = true)
    private void renderParticles(FrameGraphBuilder frameGraphBuilder, Camera camera, LightmapTextureManager lightmapTextureManager, float tickDelta, Fog fog, CallbackInfo info) {
        boolean result = EventBus.post(new RenderParticlesEvent());

        if (!result) {
            info.cancel();
        }
    }
}
