package nicotine.screens.clickgui.element.button;

import net.minecraft.client.gui.GuiGraphics;
import nicotine.screens.clickgui.element.Element;
import nicotine.util.ColorUtil;

import static nicotine.util.Common.mc;

public abstract class GUIButton extends Element {
    public String text = "";
    public int color = ColorUtil.FOREGROUND_COLOR;

    public GUIButton(String text, int x, int y) {
        super(x, y, 0, 0);
        this.text = text;
    }

    public GUIButton(int x, int y) {
        super(x, y, 0, 0);
    }

    public void click(double mouseX, double mouseY) {}

    @Override
    public void draw(GuiGraphics context, double mouseX, double mouseY) {
        context.drawString(mc.font, this.text, this.x, this.y, this.color, true);
    }

    public boolean mouseOverButton(double mouseX, double mouseY) {
        return (this.x <= mouseX && mouseX <= this.x + this.width && this.y <= mouseY && mouseY <= this.y + this.height);
    }
}
