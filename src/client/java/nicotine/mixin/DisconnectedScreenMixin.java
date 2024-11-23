package nicotine.mixin;

import net.minecraft.client.gui.screen.DisconnectedScreen;
import nicotine.events.DisconnectEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin {
    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/gui/screen/DisconnectedScreen;init()V")
    protected void init(CallbackInfo info) {
        EventBus.post(new DisconnectEvent());
    }
}
