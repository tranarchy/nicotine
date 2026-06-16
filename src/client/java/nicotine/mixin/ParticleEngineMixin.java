package nicotine.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.ParticlesRenderState;
import nicotine.events.ExtractParticleEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Inject(method = "extract", at = @At("HEAD"), cancellable = true)
    public void extract(final ParticlesRenderState particlesRenderState, final Frustum frustum, final Camera camera, final float partialTickTime, CallbackInfo info) {
        boolean result = EventBus.post(new ExtractParticleEvent());

        if (!result) {
            info.cancel();
        }
    }
}
