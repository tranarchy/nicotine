package nicotine.events;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public class GuiRenderBeforeEvent {
    public final GuiGraphicsExtractor drawContext;

    public GuiRenderBeforeEvent(GuiGraphicsExtractor drawContext) {
        this.drawContext = drawContext;
    }
}
