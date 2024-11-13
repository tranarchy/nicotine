package nicotine.mixin;

import net.minecraft.entity.Entity;

import net.minecraft.util.ActionResult;
import nicotine.events.SetVelocityClientEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import static nicotine.util.Common.*;

@Mixin(Entity.class)
public class EntityMixin {

    @Shadow
    protected UUID uuid;

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/entity/Entity;setVelocityClient(DDD)V")
    public void setVelocityClient(double x, double y, double z, CallbackInfo info)  {
        boolean result = EventBus.post(new SetVelocityClientEvent());

        if(!result && uuid == mc.player.getUuid()) {
            mc.player.addVelocity(-x, -y, -z);
        }
    }
}
