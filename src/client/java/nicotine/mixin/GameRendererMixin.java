package nicotine.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import nicotine.events.RenderBlurEvent;
import nicotine.events.RenderHandEvent;
import nicotine.events.TiltViewWhenHurtEvent;
import nicotine.util.EventBus;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/render/GameRenderer;renderBlur()V", cancellable = true)
    public void renderBlur(CallbackInfo info) {
        boolean result = EventBus.post(new RenderBlurEvent());

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setProjectionMatrix(Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/systems/ProjectionType;)V", shift = At.Shift.AFTER), method = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V")
    private void renderHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo info) {
        EventBus.post(new RenderHandEvent());
    }

    @Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/client/render/GameRenderer;tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", cancellable = true)
    private void tiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        boolean result = EventBus.post(new TiltViewWhenHurtEvent());

        if (!result) {
            info.cancel();
        }
    }

}
