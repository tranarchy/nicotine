package nicotine.util;

import net.minecraft.client.gui.DrawContext;

public class RenderGUI {
    public static void drawBorder(DrawContext drawContext, int x, int y, int width, int height, int color) {
        drawContext.drawHorizontalLine(x, x + width, y, color);
        drawContext.drawVerticalLine(x, y , y + height, color);
        drawContext.drawHorizontalLine(x, x + width, y + height, color);
        drawContext.drawVerticalLine(x + width, y , y + height, color);
    }
}