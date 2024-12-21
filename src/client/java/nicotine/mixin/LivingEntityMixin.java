package nicotine.mixin;

import net.minecraft.entity.LivingEntity;
import nicotine.mod.ModManager;
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

    @Inject(at = @At("TAIL"), method = "isGliding")
    public boolean isGliding(CallbackInfoReturnable<Boolean> info) {
        if (wasGlidin && !info.getReturnValue() && ModManager.getMod("ElytraBounce").enabled) {
            Player.startFlying();
            mc.player.startGliding();
        }

        wasGlidin = info.getReturnValue();
        return wasGlidin;
    }
}
