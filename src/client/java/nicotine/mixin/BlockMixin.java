package nicotine.mixin;

import net.minecraft.block.Block;
import nicotine.events.GetVelocityMultiplierEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/block/Block;getVelocityMultiplier()F")
    public float getVelocityMultiplier(CallbackInfoReturnable<Float> info) {
        boolean result = EventBus.post(new GetVelocityMultiplierEvent());

        if (!result)
            return 1.0f;

        return info.getReturnValue();
    }
}
