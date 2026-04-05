package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.screens.clickgui.element.misc.Text;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import static nicotine.util.Common.mc;

public class InputText extends GUIButton {
    public static InputText selectedTextBox = null;
    public String tempText;
    public String regexString = "";
    public int borderColor = ColorUtil.getPulsatingColor();

    public InputText(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.tempText = "Type here";
    }

    public InputText(int x, int y, int width, int height, String tempText) {
        this(x, y, width, height);
        this.tempText = tempText;
    }

    public InputText(int x, int y, int width, int height, String tempText, int borderColor) {
       this(x, y, width, height, tempText);
       this.borderColor = borderColor;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        String textToDraw = this.text;

        if (textToDraw.isEmpty()) {
            if (selectedTextBox == this) {
                textToDraw = "_";
            } else {
                textToDraw = String.format("%s%s", ChatFormatting.ITALIC, tempText);
            }
        } else if (selectedTextBox == this) {
            textToDraw += "_";
        }

        int charsToCut = 1;

        while(mc.font.width(textToDraw) > this.width) {
           textToDraw = textToDraw.substring(charsToCut);
           charsToCut++;
        }

        Render2D.drawBorderAroundText(context, this.x, this.y, this.width, this.height, 2, borderColor);

        context.text(mc.font, textToDraw, this.x, this.y + 1, this.color, true);
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        selectedTextBox = this;
    }
}
