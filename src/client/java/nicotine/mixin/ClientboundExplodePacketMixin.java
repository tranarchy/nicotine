package nicotine.mixin;

import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.phys.Vec3;
import nicotine.events.ExplosionKnockbackEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ClientboundExplodePacket.class)
public class ClientboundExplodePacketMixin {

    @Shadow
    Optional<Vec3> playerKnockback;

    @Inject(at = @At("HEAD"), method = "handle", cancellable = true)
    public void handle(ClientGamePacketListener clientGamePacketListener, CallbackInfo info) {
        boolean result;

        if (playerKnockback.isPresent()) {
            Vec3 kb = playerKnockback.get();
            result = EventBus.post(new ExplosionKnockbackEvent(kb.x, kb.y, kb.z));
        } else {
            result =  EventBus.post(new ExplosionKnockbackEvent(0, 0, 0));
        }

        if (!result) {
            info.cancel();
        }
    }
}
