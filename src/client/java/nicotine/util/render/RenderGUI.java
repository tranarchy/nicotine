package nicotine.util.render;

import net.minecraft.client.gui.DrawContext;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static nicotine.util.Common.*;

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

    public static Vector2f absPosToRelativePos(Vector2i pos, Vector2i size) {
        final int windowWidth = mc.getWindow().getScaledWidth();
        final int windowHeight = mc.getWindow().getScaledHeight();

        float centerX = pos.x + (size.x / 2f);
        float centerY = pos.y + (size.y / 2f);

        return new Vector2f(
                centerX / (float)windowWidth,
                centerY / (float)windowHeight
        );
    }

    public static Vector2i relativePosToAbsPos(Vector2f pos, Vector2i size) {
        final int windowWidth = mc.getWindow().getScaledWidth();
        final int windowHeight = mc.getWindow().getScaledHeight();

        return new Vector2i(
                (int) (pos.x * windowWidth) - (size.x / 2),
                (int) (pos.y * windowHeight) - (size.y / 2)
        );
    }
}
