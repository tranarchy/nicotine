package nicotine.mixin;

import net.minecraft.client.player.LocalPlayer;
import nicotine.events.InPortalEvent;
import nicotine.events.SendMovementPacketAfterEvent;
import nicotine.events.SendMovementPacketBeforeEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nicotine.util.Common.loadedChunks;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Inject(at = @At("HEAD"), method = "sendPosition")
    private void sendMovementPacketsBefore(CallbackInfo info) {
        EventBus.post(new SendMovementPacketBeforeEvent());
    }

    @Inject(at = @At("TAIL"), method = "sendPosition")
    private void sendMovementPacketsAfter(CallbackInfo info) {
        EventBus.post(new SendMovementPacketAfterEvent());
    }

    @Inject(at = @At("HEAD"), method = "handlePortalTransitionEffect", cancellable = true)
    private void handlePortalTransitionEffect(boolean fromPortalEffect, CallbackInfo info) {
        if (fromPortalEffect) {
            boolean result = EventBus.post(new InPortalEvent());

            if (!result) {
                info.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "respawn")
    public void respawn(CallbackInfo info) {
        loadedChunks.clear();
    }
}
