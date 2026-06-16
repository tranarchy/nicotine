package nicotine.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import nicotine.events.KnockbackEvent;
import nicotine.events.PushEvent;
import nicotine.events.TurnPlayerEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nicotine.util.Common.mc;

@Mixin(Entity.class)
public class EntityMixin {

    @Shadow
    private int id;

    @Inject(at = @At("HEAD"), method = "lerpMotion", cancellable = true)
    public void lerpMotion(Vec3 clientVelocity, CallbackInfo info)  {
        if (id == mc.player.getId()) {
            boolean result = EventBus.post(new KnockbackEvent(clientVelocity.x, clientVelocity.y, clientVelocity.z));

            if (!result) {
                info.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "push", cancellable = true)
    public void push(Entity entity, CallbackInfo info) {
        boolean result = EventBus.post(new PushEvent());
        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "turn", cancellable = true)
    public void turn(final double xo, final double yo, CallbackInfo info) {
        if (id != mc.player.getId())
            return;

        boolean result = EventBus.post(new TurnPlayerEvent(xo, yo));

        if (!result) {
           info.cancel();
        }
    }
}

