package nicotine.mixin;

import nicotine.events.ApplyFogCallback;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.util.ActionResult;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/render/BackgroundRenderer;applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;Lorg/joml/Vector4f;FZF)Lnet/minecraft/client/render/Fog;", cancellable = true)
    private static Fog applyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable info) {
        ActionResult result = ApplyFogCallback.EVENT.invoker().interact(camera, fogType, color, viewDistance, thickenFog, tickDelta);

        if(result == ActionResult.FAIL) {
            info.setReturnValue(new Fog(Float.MAX_VALUE, Float.MAX_VALUE, FogShape.SPHERE, color.x, color.y, color.z, color.w));
        }

       return (Fog)info.getReturnValue();
    }
}
