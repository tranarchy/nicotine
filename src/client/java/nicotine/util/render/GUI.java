package nicotine.util.render;

import net.minecraft.client.gui.DrawContext;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static nicotine.util.Common.*;

public class GUI {
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

    public static boolean mouseOver(int posX, int posY, int width, int height, double mouseX, double mouseY) {
        return (posX <= mouseX && mouseX <= posX + width && posY <= mouseY && mouseY <= posY + height);
    }

    public static Vector2i mouseDragInBounds(double mouseX, double mouseY, Vector2i dragOffset, Vector2i size) {
        final int width = mc.getWindow().getScaledWidth();
        final int height = mc.getWindow().getScaledHeight();

        Vector2i pos = new Vector2i((int) mouseX + dragOffset.x, (int) mouseY + dragOffset.y);

        if (pos.x < 0) {
            pos.x = 0;
        } else if (pos.x + size.x >= width) {
            pos.x = width - size.x - 1;
        }

        if (pos.y < 0) {
            pos.y = 0;
        } else if (pos.y + size.y >= height) {
            pos.y = height - size.y - 1;
        }

        return pos;
    }
}
