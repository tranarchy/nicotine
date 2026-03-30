package nicotine.screens.clickgui.element.button;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.screens.clickgui.element.ClickableElement;
import nicotine.screens.clickgui.element.Element;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import static nicotine.util.Common.mc;

public abstract class GUIButton extends ClickableElement {
    public String text = "";
    public int color = ColorUtil.FOREGROUND_COLOR;

    public GUIButton(String text, int x, int y) {
        super(x, y, 0, 0);
        this.text = text;
    }

    public GUIButton(int x, int y) {
        super(x, y, 0, 0);
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        context.text(mc.font, this.text, this.x, this.y, this.color, true);
    }

    public void drawUnderline(GuiGraphicsExtractor context) {
        context.horizontalLine(this.x, this.x + this.width - 1, this.y + mc.font.lineHeight + 1, ColorUtil.ACTIVE_FOREGROUND_COLOR);
    }
}
