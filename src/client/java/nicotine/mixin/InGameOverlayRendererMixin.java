package nicotine.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import nicotine.events.RenderOverlaysEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    @Inject(at = @At("HEAD"), method = "renderInWallOverlay", cancellable = true)
    private static void renderInWallOverlay(Sprite sprite, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info) {
        boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result && info != null) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderUnderwaterOverlay", cancellable = true)
    private static void renderUnderwaterOverlay(MinecraftClient client, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info) {
        boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result && info != null) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderFireOverlay", cancellable = true)
    private static void renderFireOverlay(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info) {
        boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result && info != null) {
            info.cancel();
        }
    }
}

