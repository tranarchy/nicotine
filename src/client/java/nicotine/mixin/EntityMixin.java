package nicotine.mixin;

import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import nicotine.events.KnockbackEvent;
import nicotine.events.PushEvent;
import nicotine.mod.ModManager;
import nicotine.mod.mods.render.Chams;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(at = @At("HEAD"), method = "isCurrentlyGlowing", cancellable = true)
    public void isCurrentlyGlowing(CallbackInfoReturnable<Boolean> info) {
        if (ModManager.getMod("Chams").enabled && Chams.outline.enabled && mc.level != null && mc.level.getEntity(id) instanceof RemotePlayer) {
            info.setReturnValue(true);
        }
    }
}

