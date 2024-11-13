package nicotine.events;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;

public class DrawMouseoverTooltipEvent {
    public final DrawContext drawContext;
    public final int x;
    public final int y;
    public final Slot focusedSlot;

    public DrawMouseoverTooltipEvent(DrawContext drawContext, int x, int y, Slot focusedSlot) {
        this.drawContext = drawContext;
        this.x = x;
        this.y = y;
        this.focusedSlot = focusedSlot;
    }


}
