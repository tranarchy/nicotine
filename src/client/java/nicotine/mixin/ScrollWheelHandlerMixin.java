package nicotine.mixin;

import net.minecraft.client.ScrollWheelHandler;
import nicotine.events.MouseScrollEvent;
import nicotine.util.EventBus;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScrollWheelHandler.class)
public class ScrollWheelHandlerMixin {
    @Inject(method = "onMouseScroll", at = @At("RETURN"), cancellable = true)
    public void onMouseScroll(final double scaledXScrollOffset, final double scaledYScrollOffset, CallbackInfoReturnable<Vector2i> callbackInfoReturnable) {
        boolean result = EventBus.post(new MouseScrollEvent(callbackInfoReturnable.getReturnValue()));

        if (!result) {
            callbackInfoReturnable.setReturnValue(new Vector2i(0, 0));
        }
    }
}
