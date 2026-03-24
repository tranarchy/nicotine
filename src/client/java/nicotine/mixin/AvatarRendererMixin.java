package nicotine.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import nicotine.events.SubmitNameTagEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Inject(at = @At("HEAD"), method = "submitNameDisplay", cancellable = true)
    protected void submitNameDisplay(final AvatarRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final net.minecraft.client.renderer.state.level.CameraRenderState camera, CallbackInfo info) {
        boolean result = EventBus.post(new SubmitNameTagEvent());

        if(!result) {
            info.cancel();
        }
    }
}
