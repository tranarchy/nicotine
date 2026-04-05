package nicotine.screens.clickgui.element.window;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import nicotine.screens.clickgui.element.ClickableElement;
import nicotine.screens.clickgui.element.Element;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static nicotine.util.Common.mc;

public class Window extends ClickableElement {
    private final List<Element> elements = new ArrayList<>();

    public int scrollOffset = 0;

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

    public void add(List<? extends Element> elements) {
        this.elements.addAll(elements);
    }

    public void centerPosition() {
        this.x = (mc.getWindow().getGuiScaledWidth() / 2) - (this.width / 2);
        this.y = (mc.getWindow().getGuiScaledHeight() / 2) - (this.height / 2);
    }

    public List<Element> getElements(boolean includeSelf) {
        if (!includeSelf)
            return this.elements;

        List<Element> elementsWithSelf = new ArrayList<>(elements);
        elementsWithSelf.add(this);

        return elementsWithSelf;
    }

    public List<Element> getElements() {
        return getElements(false);
    }

    public void applyZIndex() {
        this.elements.sort(Comparator.comparing(x -> x.z));
    }

    public void addDrawables() {}

    public boolean handleKeyPress(KeyEvent keyEvent) {
        return false;
    }

    public void clear() {
        this.elements.clear();
    }
}
