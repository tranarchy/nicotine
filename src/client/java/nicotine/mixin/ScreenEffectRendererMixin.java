package nicotine.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import nicotine.events.RenderOverlaysEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(at = @At("HEAD"), method = "renderTex", cancellable = true)
    private static void renderTex(TextureAtlasSprite textureAtlasSprite, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
        boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result && info != null) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderWater", cancellable = true)
    private static void renderWater(Minecraft minecraft, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
        boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result && info != null) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderFire", cancellable = true)
    private static void renderFire(PoseStack poseStack, MultiBufferSource multiBufferSource, TextureAtlasSprite textureAtlasSprite, CallbackInfo info) {
        boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result && info != null) {
            info.cancel();
        }
    }
}

