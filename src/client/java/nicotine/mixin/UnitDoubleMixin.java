package nicotine.mixin;

import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(OptionInstance.UnitDouble.class)
public class UnitDoubleMixin {

    @Inject(at = @At(value = "RETURN"), method = "validateValue", cancellable = true)
    public void validateValue(Double double_, CallbackInfoReturnable<Optional<Double>> info) {
        info.setReturnValue(Optional.of(double_));
    }
}