package nicotine.mixin;

import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleOption.class)
public class SimpleOptionMixin {

    @Shadow
    Object value;
    Text text;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/option/SimpleOption;setValue(Ljava/lang/Object;)V", cancellable = true)
    public void setValue(Object val, CallbackInfo info) {
        if (text.toString().contains("gamma")) {
            value = val;
            info.cancel();
        }
    }
}
