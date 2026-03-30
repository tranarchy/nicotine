package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.screens.clickgui.element.window.subwindow.SubWindow;


public class SubWindowButton extends GUIButton {
    protected final BaseScreen screen;
    protected final String title;
    protected SubWindow subWindow;

    public SubWindowButton(BaseScreen screen, String text, String title, int x, int y) {
        super(text, x, y);
        this.screen = screen;
        this.title = title;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        super.draw(context, mouseX, mouseY);

        if (mouseOverElement(mouseX, mouseY)) {
            drawUnderline(context);
        }
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        this.screen.addSubWindow(this.subWindow);
        this.subWindow.x = (int)mouseX;
        this.subWindow.y = (int)mouseY;
    }

    public void setSubWindow(SubWindow subWindow) {
        this.subWindow = subWindow;
    }
}
