package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.mod.mods.general.GUI;
import nicotine.mod.option.RGBOption;
import nicotine.screens.clickgui.ColorSelectionScreen;
import nicotine.screens.clickgui.element.misc.Square;
import nicotine.util.render.Render2D;

import static nicotine.util.Common.mc;

public class RGBButton extends GUIButton {
    private final RGBOption rgbOption;
    private final Square colorSquare;
    private final String title;

    public RGBButton(String title, RGBOption rgbOption, int x, int y) {
        super(rgbOption.name, x, y);
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
        this.title = title;
        this.rgbOption = rgbOption;

        this.colorSquare = new Square(x + width + 3, y - 1, 8, 8, rgbOption.getColor());
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        super.draw(context, mouseX, mouseY);

        if (mouseOverElement(mouseX, mouseY)) {
            drawUnderline(context);
        }

        colorSquare.draw(context, mouseX, mouseY);
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        ColorSelectionScreen colorSelectionScreen = new ColorSelectionScreen(GUI.screen, title, rgbOption);
        colorSelectionScreen.x = (int)mouseX;
        colorSelectionScreen.y = (int)mouseY;

        GUI.screen.addSubWindow(colorSelectionScreen);
    }

    @Override
    public boolean mouseOverElement(double mouseX, double mouseY) {
        return Render2D.mouseOver(this.x, this.y, this.width + colorSquare.width + 3, this.height, mouseX, mouseY);
    }
}
