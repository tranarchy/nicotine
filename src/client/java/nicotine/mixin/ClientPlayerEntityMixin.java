package nicotine.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import nicotine.events.InPortalEvent;
import nicotine.events.SendMovementPacketAfterEvent;
import nicotine.events.SendMovementPacketBeforeEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nicotine.util.Common.*;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "sendMovementPackets")
    private void sendMovementPacketsBefore(CallbackInfo info) {
        EventBus.post(new SendMovementPacketBeforeEvent());
    }

    @Inject(at = @At("TAIL"), method = "sendMovementPackets")
    private void sendMovementPacketsAfter(CallbackInfo info) {
        EventBus.post(new SendMovementPacketAfterEvent());
    }

    @Inject(at = @At("HEAD"), method = "tickNausea", cancellable = true)
    private void tickNausea(boolean fromPortalEffect, CallbackInfo info) {
        if (fromPortalEffect) {
            boolean result = EventBus.post(new InPortalEvent());

            if (!result) {
                info.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "requestRespawn")
    public void requestRespawn(CallbackInfo info) {
        loadedChunks.clear();
    }
}
