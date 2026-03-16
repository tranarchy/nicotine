package nicotine.screens.clickgui.element.misc;

import net.minecraft.client.gui.GuiGraphics;
import nicotine.screens.clickgui.element.Element;
import nicotine.util.ColorUtil;
import nicotine.util.render.GUI;

public class Square extends Element {

    private final int color;

    public Square(int x, int y, int width, int height, int color) {
        super(x, y, width, height);

        this.color = color;
    }

    @Override
    public void draw(GuiGraphics context, double mouseX, double mouseY) {
        context.fill(x, y, x + width, y + height, color);
        GUI.drawBorder(context, x, y, width, height, ColorUtil.FOREGROUND_COLOR);
    }
}
