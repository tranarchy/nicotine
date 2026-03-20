package nicotine.screens.clickgui.element.misc;

import net.minecraft.client.gui.GuiGraphics;
import nicotine.screens.clickgui.element.Element;

public class HLine extends Element {

    private final int color;

    public HLine(int x, int y, int width, int color) {
        super(x, y, width, 0);

        this.color = color;
    }

    @Override
    public void draw(GuiGraphics context, double mouseX, double mouseY) {
        context.hLine(x, x + width, y, color);
    }
}
