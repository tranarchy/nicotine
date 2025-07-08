package nicotine.mixin;

import net.minecraft.client.render.RenderLayer;
import nicotine.events.RenderBlockDamageEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayer.class)
public class RenderLayerMixin {

    @Inject(at = @At("TAIL"), method = "hasCrumbling", cancellable = true)
    public boolean hasCrumbling(CallbackInfoReturnable<Boolean> info) {
        boolean result = EventBus.post(new RenderBlockDamageEvent());

        if (!result) {
            info.setReturnValue(false);
        }

        return info.getReturnValue();
    }
}
