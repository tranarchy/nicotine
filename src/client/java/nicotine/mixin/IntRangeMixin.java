package nicotine.mixin;

import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(OptionInstance.IntRange.class)
public class IntRangeMixin {

    @Inject(at = @At(value = "TAIL"), method = "validateValue")
    public Optional<Integer> validateValue(Integer integer, CallbackInfoReturnable<Integer> info) {
        return Optional.of(integer);
    }
}
