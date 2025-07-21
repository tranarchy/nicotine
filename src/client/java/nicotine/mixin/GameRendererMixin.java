package nicotine.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RawProjectionMatrix;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nicotine.events.RenderHandEvent;
import nicotine.events.TotemAnimationEvent;
import nicotine.util.EventBus;
import org.joml.Matrix4f;
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
    private RawProjectionMatrix worldProjectionMatrix;

    @Inject(method = "renderHand", at = @At(value = "HEAD"))
    private void renderHand(float tickProgress, boolean sleeping, Matrix4f positionMatrix, CallbackInfo info) {
        EventBus.post(new RenderHandEvent(worldProjectionMatrix));
    }

    @Inject(method = "showFloatingItem", at = @At(value = "HEAD"), cancellable = true)
    public void showFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.getItem() == Items.TOTEM_OF_UNDYING) {
            boolean result = EventBus.post(new TotemAnimationEvent());

            if (!result)
                info.cancel();
        }
    }
}
