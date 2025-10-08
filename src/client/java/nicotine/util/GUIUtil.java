package nicotine.util;

import org.joml.Vector2i;

import static nicotine.util.Common.mc;

public class GUIUtil {
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
