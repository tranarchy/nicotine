package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ActionResult;

public interface DrawMouseoverTooltipCallback {
    Event<DrawMouseoverTooltipCallback> EVENT = EventFactory.createArrayBacked(DrawMouseoverTooltipCallback.class,
            (listeners) -> (drawContext, x, y, focusedSlot, slotWidth) -> {
                for (DrawMouseoverTooltipCallback listener : listeners) {
                    ActionResult result = listener.interact(drawContext, x, y, focusedSlot, slotWidth);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(DrawContext drawContext, int x, int y, Slot focusedSlot, int slotWidth);
}
