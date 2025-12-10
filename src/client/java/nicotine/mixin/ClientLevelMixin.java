package nicotine.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import nicotine.events.CaughtFishEvent;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.TotemPopEvent;
import nicotine.util.EventBus;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

import static nicotine.util.Common.*;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        EventBus.post(new ClientLevelTickEvent());
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V")
    public void playLocalSound(double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch, boolean useDistance, CallbackInfo info) {
        if (SoundEvents.TOTEM_USE == sound) {
            for (AbstractClientPlayer player : mc.level.players()) {
                if (player.position().distanceToSqr(x, y, z) <= 0) {
                   EventBus.post(new TotemPopEvent(player));
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/multiplayer/ClientLevel;playSeededSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V")
    public void playSeededSound(@Nullable Entity entity, double d, double e, double f, Holder<SoundEvent> holder, SoundSource soundSource, float g, float h, long l, CallbackInfo info) {
        if (SoundEvents.FISHING_BOBBER_SPLASH == holder.value()) {
            if (mc.player.fishing != null) {
                if (entity == mc.player) {
                    EventBus.post(new CaughtFishEvent());
                }
            }
        }
    }

}
