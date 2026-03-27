package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.mod.option.RGBOption;
import nicotine.screens.clickgui.ColorSelectionScreen;
import nicotine.screens.clickgui.element.misc.Square;

import static nicotine.util.Common.mc;

public class RGBButton extends GUIButton {
    private final RGBOption rgbOption;

    public RGBButton(RGBOption rgbOption, int x, int y) {
        super(rgbOption.name, x, y);
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
        this.rgbOption = rgbOption;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        super.draw(context, mouseX, mouseY);

        if (mouseOverButton(mouseX, mouseY)) {
            drawUnderline(context);
        }

        Square color = new Square(x + width + 3, y - 1, 8, 8, rgbOption.getColor());
        color.draw(context, mouseX, mouseY);
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        mc.setScreen(new ColorSelectionScreen(rgbOption));
    }
}
