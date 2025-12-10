package nicotine.events;

import net.minecraft.client.gui.GuiGraphics;

public class GuiRenderAfterEvent {
    public final GuiGraphics drawContext;

    public GuiRenderAfterEvent(GuiGraphics drawContext) {
        this.drawContext = drawContext;
    }
}
