package nicotine.screens.clickgui.element.button;

import net.minecraft.client.gui.GuiGraphics;
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
    public void draw(GuiGraphics context, double mouseX, double mouseY) {
        super.draw(context, mouseX, mouseY);

        Square color = new Square(x + width + 3, y - 1, 8, 8, rgbOption.getColor());
        color.draw(context, mouseX, mouseY);
    }

    @Override
    public void click(double mouseX, double mouseY) {
        mc.setScreen(new ColorSelectionScreen(rgbOption));
    }
}
