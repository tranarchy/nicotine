package nicotine.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import nicotine.events.*;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class HudMixin {
    @Inject(at = @At("HEAD"), method = "extractRenderState")
    public void renderBefore(final GuiGraphicsExtractor graphics, final DeltaTracker deltaTracker, CallbackInfo info) {
        EventBus.post(new GuiRenderBeforeEvent(graphics));
    }

    @Inject(at = @At("TAIL"), method = "extractRenderState")
    public void renderAfter(final GuiGraphicsExtractor graphics, final DeltaTracker deltaTracker, CallbackInfo info) {
        EventBus.post(new GuiRenderAfterEvent(graphics));
    }

    @Inject(at = @At("HEAD"), method = "extractEffects", cancellable = true)
    private void extractEffects(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        boolean result = EventBus.post(new RenderStatusEffectsOverlayEvent());

        if(!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "extractCrosshair", cancellable = true)
    private void extractCrosshair(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        boolean result = EventBus.post(new RenderCrosshairEvent(guiGraphics));

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "extractCameraOverlays", cancellable = true)
    private void extractCameraOverlays(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        boolean result = EventBus.post(new RenderMiscOverlaysEvent());

        if(!result) {
            info.cancel();
        }
    }
}
