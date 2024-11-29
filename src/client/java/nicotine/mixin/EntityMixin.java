package nicotine.mixin;

import net.minecraft.entity.Entity;
import nicotine.events.SetVelocityClientEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import static nicotine.util.Common.mc;

@Mixin(Entity.class)
public class EntityMixin {

    @Shadow
    protected UUID uuid;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/Entity;setVelocityClient(DDD)V", cancellable = true)
    public void setVelocityClient(double x, double y, double z, CallbackInfo info)  {
        if (uuid == mc.player.getUuid()) {
            boolean result = EventBus.post(new SetVelocityClientEvent(x, y, z));

            if (!result) {
                info.cancel();
            }
        }
    }
}
