package nicotine.screens.clickgui.element.button;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import static nicotine.util.Common.mc;

public class InputText extends GUIButton {
    public static boolean captureInput = false;
    public static String text = "";

    public InputText(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        String textToDraw = text;

        if (textToDraw.isEmpty()) {
            if (captureInput) {
                textToDraw = "_";
            } else {
                textToDraw = String.format("%s%s", ChatFormatting.ITALIC, "Type here");
            }
        } else if (captureInput) {
            textToDraw += "_";
        }

        context.text(mc.font, textToDraw, this.x, this.y, this.color, true);
    }

    public static void reset() {
        captureInput = false;
        text = "";
    }

    @Override
    public void click(double mouseX, double mouseY) {
        captureInput = true;
    }
}
