package nicotine.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.TotemPopEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

import static nicotine.util.Common.*;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/world/ClientWorld;tick(Ljava/util/function/BooleanSupplier;)V")
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        EventBus.post(new ClientWorldTickEvent());
    }


    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/world/ClientWorld;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V")
    private void playSound(double x, double y, double z, SoundEvent event, SoundCategory category, float volume, float pitch, boolean useDistance, CallbackInfo info) {
        if (SoundEvents.ITEM_TOTEM_USE.id() == event.id()) {
            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (player.getPos().squaredDistanceTo(x, y, z) <= 0) {
                   EventBus.post(new TotemPopEvent(player));
                }
            }
        }
    }

}
