package nicotine.events;

import net.minecraft.client.gui.DrawContext;

public class InGameHudRenderEvent {
    public final DrawContext drawContext;

    public InGameHudRenderEvent(DrawContext drawContext) {
        this.drawContext = drawContext;
    }
}
