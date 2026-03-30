package nicotine.util.render;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.screens.clickgui.element.Element;
import nicotine.util.ColorUtil;
import org.joml.Vector2i;

import static nicotine.util.Common.*;

public class Render2D {
    public static void drawBorderAroundText(GuiGraphicsExtractor context, int x, int y, int width, int height, int padding, int color) {
        int borderX = x - padding - Math.round((float) padding / 2);
        int borderY = y - padding - Math.round((float) padding / 2);

        int borderWidth = width + 2 * padding;
        int borderHeight = height + padding * (padding > 1 ? 2 : 1);

        context.fill(borderX, borderY, borderX + borderWidth, borderY + borderHeight, ColorUtil.BACKGROUND_COLOR);
        Render2D.drawBorder(context, borderX, borderY, borderWidth, borderHeight, color);
    }

    public static void drawBorderAroundText(GuiGraphicsExtractor context, String text, int x, int y, int padding, int color) {
       drawBorderAroundText(context, x, y, mc.font.width(text), mc.font.lineHeight, padding, color);
    }

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
}
