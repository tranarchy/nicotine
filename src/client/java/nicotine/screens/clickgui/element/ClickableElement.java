package nicotine.screens.clickgui.element;

import nicotine.util.render.Render2D;

public class ClickableElement extends Element {
    public ClickableElement(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean mouseOverElement(double mouseX, double mouseY) {
        return Render2D.mouseOver(this.x, this.y, this.width, this.height, mouseX, mouseY);
    }

    public void click(double mouseX, double mouseY, int input) {}
}
