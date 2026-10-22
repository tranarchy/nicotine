package nicotine.mixin;

import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import nicotine.events.FluidFaceOccludedByStateEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    @Inject(at = @At("RETURN"), method = "isFaceOccludedByState", cancellable = true)
    private static void isFaceOccludedByState(final Direction direction, final float height, final BlockState state, CallbackInfoReturnable<Boolean> info) {
        boolean result = EventBus.post(new FluidFaceOccludedByStateEvent());

        if(!result) {
            info.setReturnValue(true);
        }
    }
}
