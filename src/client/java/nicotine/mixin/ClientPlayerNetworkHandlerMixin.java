package nicotine.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import nicotine.events.SendMessageEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayerNetworkHandlerMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendChatMessage(Ljava/lang/String;)V", cancellable = true)
    public void sendChatMessage(String content, CallbackInfo info) {
        boolean result = EventBus.post(new SendMessageEvent(content));

        if (!result) {
            info.cancel();
        }
    }
}
