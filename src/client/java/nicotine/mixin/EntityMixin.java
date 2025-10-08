package nicotine.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.KnockbackEvent;
import nicotine.events.PushEvent;
import nicotine.mod.ModManager;
import nicotine.mod.mods.render.GlowESP;
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

    @Inject(at = @At("HEAD"), method = "setVelocityClient", cancellable = true)
    public void setVelocityClient(Vec3d clientVelocity, CallbackInfo info)  {
        if (id == mc.player.getId()) {
            boolean result = EventBus.post(new KnockbackEvent(clientVelocity.x, clientVelocity.y, clientVelocity.z));

            if (!result) {
                info.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/Entity;pushAwayFrom(Lnet/minecraft/entity/Entity;)V", cancellable = true)
    public void pushAwayFrom(Entity entity, CallbackInfo info) {
        boolean result = EventBus.post(new PushEvent());
        if (!result) {
            info.cancel();
        }
    }


    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/Entity;isGlowing()Z", cancellable = true)
    public void isGlowing(CallbackInfoReturnable<Boolean> info) {
        if (GlowESP.glowESP.enabled &&  mc.world.getEntityById(id) instanceof OtherClientPlayerEntity) {
            info.setReturnValue(true);
        }
    }
}

