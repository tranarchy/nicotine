package nicotine.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import nicotine.events.RenderOverlaysEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", cancellable = true)
    private static void renderOverlays(MinecraftClient client, MatrixStack matrices, CallbackInfo info) {
       boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result) {
            info.cancel();
        }
    }
}

