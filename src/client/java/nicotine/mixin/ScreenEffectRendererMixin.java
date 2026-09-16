package nicotine.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import nicotine.events.RenderOverlaysEvent;
import nicotine.events.TotemAnimationEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

   @Inject(at = @At("HEAD"), method = "submitBlockSprite", cancellable = true)
   private static void submitBlockSprite(final Identifier atlasLocation, final float u0, final float v0, final float u1, final float v1, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int color, CallbackInfo info) {
        boolean result = EventBus.post(new RenderOverlaysEvent());

        if (!result && info != null) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "submitWater", cancellable = true)
    private static void submitWater(final PlayerRenderState.WaterOverlay waterOverlay, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, CallbackInfo info) {
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

    @Inject(at = @At("HEAD"), method = "renderItemActivationAnimation", cancellable = true)
    private void renderItemActivationAnimation(final PlayerRenderState playerRenderState, final PoseStack poseStack, final float partialTicks, final SubmitNodeCollector submitNodeCollector, CallbackInfo info) {
        boolean result = EventBus.post(new TotemAnimationEvent());

        if (!result)
            info.cancel();
    }
}

