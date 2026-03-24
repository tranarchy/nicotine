package nicotine.events;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public class RenderCrosshairEvent {
    public GuiGraphicsExtractor context;

    public RenderCrosshairEvent(GuiGraphicsExtractor context) {
        this.context = context;
    }
}
