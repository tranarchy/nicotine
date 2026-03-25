package nicotine.screens.clickgui.element.misc;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.screens.clickgui.element.Element;
import nicotine.util.ColorUtil;

import static nicotine.util.Common.mc;

public class Text extends Element {
    public String text = "";
    public int color = ColorUtil.FOREGROUND_COLOR;

    public Text(String text, int x, int y) {
        super(x, y, 0, 0);
        this.text = text;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        context.text(mc.font, this.text, this.x, this.y, this.color, true);
    }
}
