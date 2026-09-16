package nicotine.mixin;

import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import nicotine.events.RenderHandEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Final
    @Shadow
    private ProjectionMatrixBuffer levelProjectionMatrixBuffer;

    @Inject(method = "renderItemInHand", at = @At(value = "HEAD"))
    private void renderItemInHand(final CameraRenderState cameraState, final PlayerRenderState playerState, final GpuTextureView depthTextureView, CallbackInfo info) {
        EventBus.post(new RenderHandEvent(levelProjectionMatrixBuffer));
    }
}
