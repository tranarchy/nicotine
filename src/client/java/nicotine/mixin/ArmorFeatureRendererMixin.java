package nicotine.mixin;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import nicotine.events.RenderArmorEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @Inject(at = @At("HEAD"), method = "renderArmor", cancellable = true)
    private void renderArmor(MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, ItemStack stack, EquipmentSlot slot, int light, BipedEntityRenderState bipedEntityRenderState, CallbackInfo info) {
        if (bipedEntityRenderState instanceof PlayerEntityRenderState) {
            boolean result = EventBus.post(new RenderArmorEvent());

            if (!result)
                info.cancel();
        }
    }
}
