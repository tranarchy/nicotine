package nicotine.mixin;

import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;
import nicotine.events.KnockbackEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ExplosionS2CPacket.class)
public class ExplosionS2CPacketMixin {

    @Shadow
    Optional<Vec3d> playerKnockback;

    @Inject(at = @At("HEAD"), method = "apply", cancellable = true)
    public void apply(ClientPlayPacketListener clientPlayPacketListener, CallbackInfo info) {
        boolean result;

        if (playerKnockback.isPresent()) {
            Vec3d kb = playerKnockback.get();
            result =  EventBus.post(new KnockbackEvent(kb.x, kb.y, kb.z));
        } else {
            result =  EventBus.post(new KnockbackEvent(0, 0, 0));
        }

        if (!result) {
            info.cancel();
        }
    }
}
