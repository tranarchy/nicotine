package nicotine.mixin;

import net.minecraft.client.Minecraft;
import nicotine.events.ClientTickEvent;
import nicotine.events.FinishedLoadingEvent;
import nicotine.events.AllowsTelemetryEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    private boolean gameLoadFinished;

    @Inject(at = @At("TAIL"), method = "onResourceLoadFinished")
    private void onResourceLoadFinished(CallbackInfo info) {
        if (gameLoadFinished) {
            EventBus.post(new FinishedLoadingEvent());
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        EventBus.post(new ClientTickEvent());
    }

    @Inject(at = @At("TAIL"), method = "allowsTelemetry")
    public boolean allowsTelemetry(CallbackInfoReturnable<Boolean> info) {
        boolean result = EventBus.post(new AllowsTelemetryEvent());

        if (!result)
            return false;

        return info.getReturnValue();
    }
}
