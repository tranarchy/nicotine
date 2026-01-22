package nicotine.mixin;

import net.minecraft.world.entity.LivingEntity;
import nicotine.mod.ModManager;
import nicotine.mod.mods.movement.ElytraBounce;
import nicotine.util.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static nicotine.util.Common.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Unique
    private boolean wasGlidin = false;

    @Inject(at = @At("TAIL"), method = "isFallFlying")
    public boolean isFallFlying(CallbackInfoReturnable<Boolean> info) {
        if (wasGlidin && !info.getReturnValue() && ModManager.getMod("ElytraBounce").enabled) {
            Player.startFlying();
            mc.player.startFallFlying();
        }

        wasGlidin = info.getReturnValue();
        return wasGlidin;
    }
}
