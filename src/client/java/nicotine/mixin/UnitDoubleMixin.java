package nicotine.mixin;

import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(OptionInstance.UnitDouble.class)
public class UnitDoubleMixin {

    @Inject(at = @At(value = "TAIL"), method = "validateValue")
    public Optional<Double> validateValue(Double double_, CallbackInfoReturnable<Double> info) {
        return Optional.of(double_);
    }
}