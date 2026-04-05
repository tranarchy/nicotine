package nicotine.screens.clickgui.element.button;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.screens.clickgui.element.ClickableElement;
import nicotine.screens.clickgui.element.Element;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import static nicotine.util.Common.mc;

public class GUIButton extends ClickableElement {
    public String text = "";
    public int color = ColorUtil.FOREGROUND_COLOR;
    public int borderPadding = 0;
    public int borderColor = ColorUtil.getPulsatingColor();

    public GUIButton(String text, int x, int y) {
        super(x, y, mc.font.width(text), mc.font.lineHeight);
        this.text = text;
    }

    public GUIButton(int x, int y) {
        super(x, y, 0, 0);
    }

    public GUIButton(String text, int x, int y, int borderPadding) {
        super(x, y, mc.font.width(text), mc.font.lineHeight);
        this.text = text;
        this.borderPadding = borderPadding;
    }

    public GUIButton(String text, int x, int y, int borderPadding, int borderColor) {
        super(x, y, mc.font.width(text), mc.font.lineHeight);
        this.text = text;
        this.borderPadding = borderPadding;
        this.borderColor = borderColor;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        if (borderPadding != 0) {
            Render2D.drawBorderAroundText(context, this.x, this.y, this.width, this.height, borderPadding, borderColor);
        }

        context.text(mc.font, this.text, this.x, this.y, this.color, true);

        if (this.mouseOverElement(mouseX, mouseY) && borderPadding == 0) {
            this.drawUnderline(context);
        }
    }

    public void drawUnderline(GuiGraphicsExtractor context) {
        context.horizontalLine(this.x, this.x + this.width - 1, this.y + mc.font.lineHeight + 1, ColorUtil.ACTIVE_FOREGROUND_COLOR);
    }
}
