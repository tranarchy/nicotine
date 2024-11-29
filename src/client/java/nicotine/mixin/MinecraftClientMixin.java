package nicotine.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.realms.RealmsClient;
import nicotine.events.ClientTickEvent;
import nicotine.events.FinishedLoadingEvent;
import nicotine.events.IsTelemetryEnabledByApiEvent;
import nicotine.util.EventBus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.nicotine;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    private boolean finishedLoading;

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/MinecraftClient;onFinishedLoading(Lnet/minecraft/client/MinecraftClient$LoadingContext;)V")
    private void onFinishedLoading(CallbackInfo info) {
        if (finishedLoading) {
            EventBus.post(new FinishedLoadingEvent());
        }
    }

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
}
