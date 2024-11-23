package nicotine.mixin;

import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SimpleOption.DoubleSliderCallbacks.class)
public class DoubleSliderCallbacksMixin {

    @Inject(at = @At(value = "TAIL"), method = "Lnet/minecraft/client/option/SimpleOption$DoubleSliderCallbacks;validate(Ljava/lang/Double;)Ljava/util/Optional;")
    public Optional<Double> validate(Double double_, CallbackInfoReturnable<Double> info) {
        return Optional.of(double_);
    }
}