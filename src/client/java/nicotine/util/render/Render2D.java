package nicotine.util.render;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Vector2i;

import static nicotine.util.Common.*;

public class Render2D {
    public static void drawBorder(GuiGraphicsExtractor drawContext, int x, int y, int width, int height, int color) {
        drawBorderVertical(drawContext, x, y, width, height, color);
        drawBorderHorizontal(drawContext, x, y, width, height, color);
    }

    public static void drawBorderVertical(GuiGraphicsExtractor drawContext, int x, int y, int width, int height, int color) {
        drawContext.verticalLine(x, y , y + height, color);
        drawContext.verticalLine(x + width, y, y + height, color);
    }

    public static void drawBorderHorizontal(GuiGraphicsExtractor drawContext, int x, int y, int width, int height, int color) {
        drawContext.horizontalLine(x, x + width, y, color);
        drawContext.horizontalLine(x, x + width, y + height, color);
    }

    public static boolean mouseOver(int posX, int posY, int width, int height, double mouseX, double mouseY) {
        return (posX <= mouseX && mouseX <= posX + width && posY <= mouseY && mouseY <= posY + height);
    }

    public static Vector2i mouseDragInBounds(double mouseX, double mouseY, Vector2i dragOffset, Vector2i size) {
        final int width = mc.getWindow().getGuiScaledWidth();
        final int height = mc.getWindow().getGuiScaledHeight();

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
