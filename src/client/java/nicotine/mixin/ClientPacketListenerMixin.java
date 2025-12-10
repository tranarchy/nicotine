package nicotine.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import nicotine.events.SendMessageEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(at = @At("HEAD"), method = "sendChat", cancellable = true)
    public void sendChat(String content, CallbackInfo info) {
        boolean result = EventBus.post(new SendMessageEvent(content));

        if (!result) {
            info.cancel();
        }
    }
}
