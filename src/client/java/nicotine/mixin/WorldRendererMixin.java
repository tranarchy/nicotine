package nicotine.mixin;

import nicotine.events.RenderEntityCallback;
import nicotine.events.RenderWeatherCallback;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", cancellable = true)
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info) {
        ActionResult result = RenderEntityCallback.EVENT.invoker().interact(entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers);
        if(result == ActionResult.FAIL) {
            info.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/render/WorldRenderer;renderWeather(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Vec3d;FLnet/minecraft/client/render/Fog;)V", cancellable = true)
    private void renderWeather(FrameGraphBuilder frameGraphBuilder, LightmapTextureManager lightmapTextureManager, Vec3d pos, float tickDelta, Fog fog, CallbackInfo info) {
        ActionResult result = RenderWeatherCallback.EVENT.invoker().interact(frameGraphBuilder, lightmapTextureManager, pos, tickDelta, fog);

        if(result == ActionResult.FAIL) {
            info.cancel();
        }
    }

}
