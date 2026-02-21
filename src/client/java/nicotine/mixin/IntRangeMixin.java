package nicotine.mixin;

import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(OptionInstance.IntRange.class)
public class IntRangeMixin {

    @Inject(at = @At(value = "RETURN"), method = "validateValue", cancellable = true)
    public void validateValue(Integer integer, CallbackInfoReturnable<Optional<Integer>> info) {
        info.setReturnValue(Optional.of(integer));
    }
}
