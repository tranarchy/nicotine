package nicotine.mixin;

import net.minecraft.client.renderer.rendertype.RenderType;
import nicotine.events.RenderBlockDamageEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderType.class)
public class RenderTypeMixin {

    @Inject(at = @At("TAIL"), method = "affectsCrumbling", cancellable = true)
    public void affectsCrumbling(CallbackInfoReturnable<Boolean> info) {
        boolean result = EventBus.post(new RenderBlockDamageEvent());
        info.setReturnValue(result);
    }
}
