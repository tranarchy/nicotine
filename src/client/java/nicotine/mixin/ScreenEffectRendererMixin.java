package nicotine.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import nicotine.events.RenderOverlaysEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

   @Inject(at = @At("HEAD"), method = "submitBlockSprite", cancellable = true)
   private static void submitBlockSprite(final TextureAtlasSprite sprite, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int color, CallbackInfo info) {
        boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result && info != null) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "submitWater", cancellable = true)
    private static void submitWater(final Minecraft minecraft, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, CallbackInfo info) {
        boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result && info != null) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "submitFire", cancellable = true)
    private static void submitFire(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final TextureAtlasSprite sprite, CallbackInfo info) {
        boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result && info != null) {
            info.cancel();
        }
    }
}

