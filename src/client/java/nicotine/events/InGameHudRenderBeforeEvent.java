package nicotine.events;

import net.minecraft.client.gui.DrawContext;

public class InGameHudRenderBeforeEvent {
    public final DrawContext drawContext;

    public InGameHudRenderBeforeEvent(DrawContext drawContext) {
        this.drawContext = drawContext;
    }
}
