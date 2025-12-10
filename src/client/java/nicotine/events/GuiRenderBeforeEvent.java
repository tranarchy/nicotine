package nicotine.events;

import net.minecraft.client.gui.GuiGraphics;

public class GuiRenderBeforeEvent {
    public final GuiGraphics drawContext;

    public GuiRenderBeforeEvent(GuiGraphics drawContext) {
        this.drawContext = drawContext;
    }
}
