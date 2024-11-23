package nicotine.mixin;

import net.minecraft.client.MinecraftClient;
import nicotine.events.ClientTickEvent;
import nicotine.events.IsTelemetryEnabledByApiEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static nicotine.util.Common.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/MinecraftClient;tick()V")
    public void tick(CallbackInfo info) {
        EventBus.post(new ClientTickEvent());
    }

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/MinecraftClient;isTelemetryEnabledByApi()Z")
    public boolean isTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> info) {
        boolean result = EventBus.post(new IsTelemetryEnabledByApiEvent());

        if (!result)
            return false;

        return info.getReturnValue();
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;updateWindowTitle()V", cancellable = true)
    public void updateWindowTitle(CallbackInfo info) {
        mc.getWindow().setTitle(String.format("nicotine v%s", nicotine.getVersion()));
        info.cancel();
    }
}
