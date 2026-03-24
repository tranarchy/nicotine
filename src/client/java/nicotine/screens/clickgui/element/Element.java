package nicotine.screens.clickgui.element;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Element {
    public int x;
    public int y;
    public int width;
    public int height;

    public Element(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {}
}
