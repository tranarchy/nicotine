package nicotine.mixin;

import net.minecraft.client.MinecraftClient;
import nicotine.events.IsTelemetryEnabledByApiEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/MinecraftClient;isTelemetryEnabledByApi()Z")
    public boolean isTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> info) {
        boolean result = EventBus.post(new IsTelemetryEnabledByApiEvent());

        if (!result)
            return false;

        return info.getReturnValue();
    }
}
