package nicotine.events;

import net.minecraft.client.gui.DrawContext;

public class InGameHudRenderAfterEvent {
    public final DrawContext drawContext;

    public InGameHudRenderAfterEvent(DrawContext drawContext) {
        this.drawContext = drawContext;
    }
}
