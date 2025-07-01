package nicotine.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import nicotine.events.CaughtFishEvent;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.TotemPopEvent;
import nicotine.util.EventBus;
import org.jetbrains.annotations.Nullable;
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


    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/world/ClientWorld;playSoundClient(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V")
    public void playSound(double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance, CallbackInfo info) {
        if (SoundEvents.ITEM_TOTEM_USE == sound) {
            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (player.getPos().squaredDistanceTo(x, y, z) <= 0) {
                   EventBus.post(new TotemPopEvent(player));
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/world/ClientWorld;playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FFJ)V")
    public void playSound(@Nullable Entity source, double x, double y, double z, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed, CallbackInfo info) {
        if (SoundEvents.ENTITY_FISHING_BOBBER_SPLASH == sound.value()) {
            if (mc.player.fishHook != null) {
                if (source == mc.player) {
                    EventBus.post(new CaughtFishEvent());
                }
            }
        }
    }

}
