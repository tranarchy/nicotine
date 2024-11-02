package nicotine.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ActionResult;
import nicotine.events.DrawMouseoverTooltipCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Shadow
    protected Slot focusedSlot;
    protected ScreenHandler handler;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawMouseoverTooltip(Lnet/minecraft/client/util/math/MatrixStack;II)V", cancellable = true)
    protected void drawMouseoverTooltip(DrawContext drawContext, int x, int y, CallbackInfo info) {
        int slotsCount = handler.slots.size();
        int slotWidth = handler.slots.get(slotsCount - 3).x - handler.slots.get(slotsCount - 4).x;

        ActionResult result = DrawMouseoverTooltipCallback.EVENT.invoker().interact(drawContext, x, y, focusedSlot, slotWidth);

        if(result == ActionResult.FAIL) {
            info.cancel();
        }
    }
}
