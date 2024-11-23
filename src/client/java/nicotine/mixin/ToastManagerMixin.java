package nicotine.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.ToastManager;
import nicotine.events.DrawToastEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class ToastManagerMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/toast/ToastManager;draw(Lnet/minecraft/client/gui/DrawContext;)V", cancellable = true)
    public void draw(DrawContext context, CallbackInfo info) {
        boolean result = EventBus.post(new DrawToastEvent());

        if (!result) {
            info.cancel();
        }
    }
}
