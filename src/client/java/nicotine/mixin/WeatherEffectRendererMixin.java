package nicotine.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderWeatherEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {
    @Inject(at = @At("HEAD"), method = "extractRenderState", cancellable = true)
    public void extractRenderState(final ClientLevel level, final float partialTicks, final Vec3 cameraPos, final WeatherRenderState renderState, CallbackInfo info) {
        boolean result = EventBus.post(new RenderWeatherEvent());

        if (!result) {
            info.cancel();
        }
    }
}
