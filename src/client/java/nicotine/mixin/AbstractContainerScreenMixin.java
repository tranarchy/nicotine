package nicotine.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import nicotine.events.AbstractContainerScreenTickEvent;
import nicotine.events.RenderTooltipEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Shadow
    protected Slot hoveredSlot;

    @Inject(at = @At("HEAD"), method = "extractTooltip", cancellable = true)
    protected void extractTooltip(GuiGraphicsExtractor drawContext, int x, int y, CallbackInfo info) {
        boolean result = EventBus.post(new RenderTooltipEvent(drawContext, x, y, hoveredSlot));

        if (!result)
            info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public final void tick(CallbackInfo info) {
        EventBus.post(new AbstractContainerScreenTickEvent(((AbstractContainerScreen) (Object)this)));
    }
}
