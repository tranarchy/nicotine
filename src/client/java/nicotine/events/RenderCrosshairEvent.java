package nicotine.events;

import net.minecraft.client.gui.DrawContext;

public class RenderCrosshairEvent {
    public DrawContext context;

    public RenderCrosshairEvent(DrawContext context) {
        this.context = context;
    }
}
