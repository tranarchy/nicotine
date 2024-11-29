package nicotine.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import nicotine.events.*;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", cancellable = true)
    private void renderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        boolean result = EventBus.post(new RenderStatusEffectsOverlayEvent());

        if(!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", cancellable = true)
    private void renderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        boolean result = EventBus.post(new RenderCrosshairEvent(context));

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
    public void renderBefore(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        EventBus.post(new InGameHudRenderBeforeEvent(context));
    }

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
    public void renderAfter(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        EventBus.post(new InGameHudRenderAfterEvent(context));
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/hud/InGameHud;renderMiscOverlays(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", cancellable = true)
    private void renderMiscOverlays(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        boolean result = EventBus.post(new RenderMiscOverlaysEvent());

        if(!result) {
            info.cancel();
        }
    }
}