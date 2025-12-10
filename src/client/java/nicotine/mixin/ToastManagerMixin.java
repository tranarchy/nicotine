package nicotine.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.ToastManager;
import nicotine.events.DrawToastEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class ToastManagerMixin {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(GuiGraphics context, CallbackInfo info) {
        boolean result = EventBus.post(new DrawToastEvent());

        if (!result) {
            info.cancel();
        }
    }
}
