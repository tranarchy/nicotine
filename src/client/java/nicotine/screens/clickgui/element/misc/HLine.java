package nicotine.screens.clickgui.element.misc;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.screens.clickgui.element.Element;

public class HLine extends Element {

    private final int color;

    public HLine(int x, int y, int width, int color) {
        super(x, y, width, 0);

        this.color = color;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        context.horizontalLine(x, x + width, y, color);
    }
}
