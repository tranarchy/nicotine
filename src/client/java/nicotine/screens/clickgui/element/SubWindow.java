package nicotine.screens.clickgui.element;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.screens.clickgui.element.button.ToggleButton;
import nicotine.screens.clickgui.element.misc.Text;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import static nicotine.util.Common.mc;

public class SubWindow extends Window implements Draggable {
    protected BaseScreen screen;

    private final Text titleText;
    protected final ToggleButton closeButton;
    protected final Window dragArea;

    @Override
    public boolean isDraggableArea(double mouseX, double mouseY) {
        return dragArea.mouseOverElement(mouseX, mouseY);
    }

    public SubWindow(BaseScreen screen, String title, int x, int y, int width, int height) {
        super(x, y, width, height);

        this.screen = screen;

        SubWindow subWindow = this;

        titleText = new Text(title, this.width, this.y) {
            @Override
            public void draw(GuiGraphicsExtractor context,double mouseX, double mouseY){
                Render2D.drawBorderAroundText(context, this.x, this.y, this.width, this.height,2, ColorUtil.getPulsatingColor());
                context.text(mc.font, this.text, this.x, this.y, ColorUtil.getPulsatingColor(), true);
            }
        };

        ToggleOption closeOption = new ToggleOption("X") {
            @Override
            public void toggle() {
                screen.removeSubWindow(subWindow);
            }
        };

        closeButton = new ToggleButton(closeOption, subWindow.x, subWindow.y) {
            @Override
            public void draw(GuiGraphicsExtractor context,double mouseX, double mouseY){
                Render2D.drawBorderAroundText(context, this.x, this.y, this.width, this.height,2, ColorUtil.getPulsatingColor());
                context.text(mc.font, this.text, this.x, this.y, ColorUtil.getPulsatingColor(), true);
            }

            @Override
            public boolean mouseOverElement(double mouseX, double mouseY) {
                return Render2D.mouseOver(this.x, this.y, this.width, this.height, mouseX, mouseY);
            }
        };

        this.dragArea = new Window(this.x, this.y, this.width, this.height);
    }

    public boolean handleKeyPress(KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        if (this.width != 0 && this.height != 0) {
            context.fill(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtil.BACKGROUND_COLOR);
            Render2D.drawBorder(context, this.x, this.y, this.width, this.height, ColorUtil.getPulsatingColor());
        }
    }

    public void addDrawables() {
        this.elements.clear();

        titleText.x = this.x + this.width - titleText.width - 1;
        titleText.y = this.y - titleText.height - 1;

        closeButton.x = this.x + (closeButton.width  / 2);
        closeButton.y = this.y - closeButton.height - 1;

        dragArea.x = this.x;
        dragArea.y = closeButton.y - 3;
        dragArea.height = closeButton.height + 4;

        this.add(dragArea);
        this.add(closeButton);
        this.add(titleText);
    }
}
