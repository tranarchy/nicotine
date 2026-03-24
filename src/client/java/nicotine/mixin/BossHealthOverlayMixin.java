package nicotine.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import nicotine.events.RenderBossBarHudEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {
    @Inject(at = @At("HEAD"), method = "extractRenderState", cancellable = true)
    public void extractRenderState(GuiGraphicsExtractor context, CallbackInfo info) {
        boolean result = EventBus.post(new RenderBossBarHudEvent());

        if (!result) {
            info.cancel();
        }
    }
}
