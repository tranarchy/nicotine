package nicotine.util.render;

import net.minecraft.client.gui.DrawContext;

public class RenderGUI {
    public static void drawBorder(DrawContext drawContext, int x, int y, int width, int height, int color) {
        drawBorderVertical(drawContext, x, y, width, height, color);
        drawBorderHorizontal(drawContext, x, y, width, height, color);
    }

    public static void drawBorderVertical(DrawContext drawContext, int x, int y, int width, int height, int color) {
        drawContext.drawVerticalLine(x, y , y + height, color);
        drawContext.drawVerticalLine(x + width, y, y + height, color);
    }

    public static void drawBorderHorizontal(DrawContext drawContext, int x, int y, int width, int height, int color) {
        drawContext.drawHorizontalLine(x, x + width, y, color);
        drawContext.drawHorizontalLine(x, x + width, y + height, color);
    }
}
