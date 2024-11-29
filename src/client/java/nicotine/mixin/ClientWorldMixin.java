package nicotine.mixin;

import net.minecraft.client.world.ClientWorld;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/world/ClientWorld;tick(Ljava/util/function/BooleanSupplier;)V")
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        EventBus.post(new ClientWorldTickEvent());
    }

}
