package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.screens.clickgui.element.misc.Text;

import static nicotine.util.Common.mc;

public class InputText extends GUIButton {
    public static InputText selectedTextBox = null;

    public InputText(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        String textToDraw = this.text;

        if (textToDraw.isEmpty()) {
            if (selectedTextBox == this) {
                textToDraw = "_";
            } else {
                textToDraw = String.format("%s%s", ChatFormatting.ITALIC, "Type here");
            }
        } else if (selectedTextBox == this) {
            textToDraw += "_";
        }

        context.text(mc.font, textToDraw, this.x, this.y, this.color, true);
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        selectedTextBox = this;
    }
}
