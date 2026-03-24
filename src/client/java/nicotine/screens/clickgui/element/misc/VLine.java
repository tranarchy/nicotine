package nicotine.screens.clickgui.element.misc;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.screens.clickgui.element.Element;

public class VLine extends Element {
    private final int color;

    public VLine(int x, int y, int height, int color) {
        super(x, y, 0, height);

        this.color = color;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        context.verticalLine(x, y, y + height, color);
    }
}
