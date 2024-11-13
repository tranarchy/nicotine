package nicotine.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ActionResult;
import nicotine.events.DrawMouseoverTooltipEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Shadow
    protected Slot focusedSlot;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawMouseoverTooltip(Lnet/minecraft/client/gui/DrawContext;II)V", cancellable = true)
    protected void drawMouseoverTooltip(DrawContext drawContext, int x, int y, CallbackInfo info) {
        boolean result = EventBus.post(new DrawMouseoverTooltipEvent(drawContext, x, y, focusedSlot));

        if (!result)
            info.cancel();
    }
}
