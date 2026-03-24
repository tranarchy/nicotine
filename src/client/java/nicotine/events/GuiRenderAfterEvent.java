package nicotine.events;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public class GuiRenderAfterEvent {
    public final GuiGraphicsExtractor drawContext;

    public GuiRenderAfterEvent(GuiGraphicsExtractor drawContext) {
        this.drawContext = drawContext;
    }
}
