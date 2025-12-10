package nicotine.events;

import net.minecraft.client.gui.GuiGraphics;

public class RenderCrosshairEvent {
    public GuiGraphics context;

    public RenderCrosshairEvent(GuiGraphics context) {
        this.context = context;
    }
}
