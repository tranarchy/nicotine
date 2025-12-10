package nicotine.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PerspectiveProjectionMatrixBuffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
    private PerspectiveProjectionMatrixBuffer levelProjectionMatrixBuffer;

    @Inject(method = "renderItemInHand", at = @At(value = "HEAD"))
    private void renderItemInHand(float f, boolean bl, Matrix4f matrix4f, CallbackInfo info) {
        EventBus.post(new RenderHandEvent(levelProjectionMatrixBuffer));
    }

    @Inject(method = "displayItemActivation", at = @At(value = "HEAD"), cancellable = true)
    public void displayItemActivation(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.getItem() == Items.TOTEM_OF_UNDYING) {
            boolean result = EventBus.post(new TotemAnimationEvent());

            if (!result)
                info.cancel();
        }
    }
}
