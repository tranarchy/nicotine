package nicotine.screens.clickgui.element.button;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.mod.option.RGBOption;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.screens.clickgui.element.window.subwindow.ColorSelectionWindow;
import nicotine.screens.clickgui.element.misc.Square;
import nicotine.util.render.Render2D;

public class RGBButton extends SubWindowButton {

    private final Square colorSquare;
    private final RGBOption rgbOption;

    public RGBButton(BaseScreen screen, String title, RGBOption rgbOption, int x, int y) {
        super(screen, rgbOption.name, title, x, y);

        this.rgbOption = rgbOption;
        this.colorSquare = new Square(x + width + 3, y - 1, 8, 8, rgbOption.getColor());
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        super.draw(context, mouseX, mouseY);
        colorSquare.draw(context, mouseX, mouseY);
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        this.setSubWindow(new ColorSelectionWindow(this.screen, this.title, rgbOption));
        super.click(mouseX, mouseY, input);
    }

    @Override
    public boolean mouseOverElement(double mouseX, double mouseY) {
        return Render2D.mouseOver(this.x, this.y, this.width + colorSquare.width + 3, this.height, mouseX, mouseY);
    }
}
