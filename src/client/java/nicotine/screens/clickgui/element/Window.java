package nicotine.screens.clickgui.element;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import java.util.ArrayList;
import java.util.List;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.window;

public class Window extends ClickableElement {
    public final List<Element> elements = new ArrayList<>();

    public Window(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        if (this.width != 0 && this.height != 0) {
            context.fill(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtil.BACKGROUND_COLOR);
            Render2D.drawBorder(context, this.x, this.y, this.width, this.height, ColorUtil.getPulsatingColor());
        }

        for (Element element : elements) {
            element.draw(context, mouseX, mouseY);
        }
    }

    public void add(Element element) {
        this.elements.add(element);
    }

    public void centerPosition() {
        this.x = (mc.getWindow().getGuiScaledWidth() / 2) - (this.width / 2);
        this.y = (mc.getWindow().getGuiScaledHeight() / 2) - (this.height / 2);
    }
}
