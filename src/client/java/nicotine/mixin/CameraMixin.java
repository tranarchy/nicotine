package nicotine.mixin;

import net.minecraft.client.Camera;
import nicotine.events.GetMaxZoomEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {
    @Inject(method = "getMaxZoom", at = @At("HEAD"), cancellable = true)
    private void getMaxZoom(float f, CallbackInfoReturnable<Float> info) {
        boolean result = EventBus.post(new GetMaxZoomEvent());

        if (!result) {
            info.setReturnValue(f);
        }
    }
}
