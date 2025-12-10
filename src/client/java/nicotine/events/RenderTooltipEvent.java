package nicotine.events;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;

public class RenderTooltipEvent {
    public final GuiGraphics drawContext;
    public final int x;
    public final int y;
    public final Slot focusedSlot;

    public RenderTooltipEvent(GuiGraphics drawContext, int x, int y, Slot focusedSlot) {
        this.drawContext = drawContext;
        this.x = x;
        this.y = y;
        this.focusedSlot = focusedSlot;
    }


}
