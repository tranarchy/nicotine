package nicotine.mixin;

import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SimpleOption.ValidatingIntSliderCallbacks.class)
public class ValidatingIntSliderCallbacksMixin {

    @Inject(at = @At(value = "TAIL"), method = "Lnet/minecraft/client/option/SimpleOption$ValidatingIntSliderCallbacks;validate(Ljava/lang/Integer;)Ljava/util/Optional;")
    public Optional<Integer> validate(Integer integer, CallbackInfoReturnable<Integer> info) {
        return Optional.of(integer);
    }
}
