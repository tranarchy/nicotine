package nicotine.screens.clickgui.element;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class Element {
    public int x;
    public int y;
    public int width;
    public int height;
    public int z;

    public Element(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.z = 0;
    }

    public abstract void draw(GuiGraphicsExtractor context, double mouseX, double mouseY);
}
