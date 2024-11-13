package nicotine.mixin;

import net.minecraft.block.*;
import nicotine.events.GetRenderTypeEvent;
import net.minecraft.util.ActionResult;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/block/AbstractBlock;getRenderType(Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockRenderType;")
    public BlockRenderType getRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> info) {
        boolean result = EventBus.post(new GetRenderTypeEvent(state));

        if(!result) {
            return BlockRenderType.INVISIBLE;
        }

        return info.getReturnValue();
    }
}
