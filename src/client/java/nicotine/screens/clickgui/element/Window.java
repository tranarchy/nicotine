package nicotine.screens.clickgui.element;

import net.minecraft.client.gui.GuiGraphics;
import nicotine.util.ColorUtil;
import nicotine.util.render.GUI;

import java.util.ArrayList;
import java.util.List;

import static nicotine.util.Common.mc;

public class Window extends Element {
    public final List<Element> elements = new ArrayList<>();

    public Window(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(GuiGraphics context, double mouseX, double mouseY) {
        int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

        context.fill(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtil.BACKGROUND_COLOR);
        GUI.drawBorder(context, this.x, this.y, this.width, this.height, dynamicColor);

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
