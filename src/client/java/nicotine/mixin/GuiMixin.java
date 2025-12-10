package nicotine.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import nicotine.events.*;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(at = @At("HEAD"), method = "renderEffects", cancellable = true)
    private void renderEffects(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        boolean result = EventBus.post(new RenderStatusEffectsOverlayEvent());

        if(!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    private void renderCrosshair(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        boolean result = EventBus.post(new RenderCrosshairEvent(guiGraphics));

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "render")
    public void renderBefore(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        EventBus.post(new GuiRenderBeforeEvent(guiGraphics));
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void renderAfter(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        EventBus.post(new GuiRenderAfterEvent(guiGraphics));
    }

    @Inject(at = @At("HEAD"), method = "renderCameraOverlays", cancellable = true)
    private void renderCameraOverlays(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        boolean result = EventBus.post(new RenderMiscOverlaysEvent());

        if(!result) {
            info.cancel();
        }
    }
}