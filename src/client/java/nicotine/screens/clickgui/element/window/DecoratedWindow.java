package nicotine.screens.clickgui.element.window;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.screens.clickgui.element.Draggable;
import nicotine.screens.clickgui.element.button.GUIButton;
import nicotine.screens.clickgui.element.misc.Text;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;
import org.jspecify.annotations.Nullable;

import static nicotine.util.Common.mc;

public class DecoratedWindow extends Window implements Draggable {
    protected BaseScreen screen;

    private final Text titleText;
    protected GUIButton closeButton;
    protected final Window dragArea;

    @Override
    public boolean isDraggableArea(double mouseX, double mouseY) {
        return dragArea.mouseOverElement(mouseX, mouseY);
    }

    protected void close() {
        if (screen != null)
            screen.removeSubWindow(this);
        else
            mc.setScreen(null);
    }

    public DecoratedWindow(@Nullable BaseScreen screen, String title, int x, int y, int width, int height) {
        super(x, y, width, height);

        this.screen = screen;

        titleText = new Text(title, this.width, this.y) {
            @Override
            public void draw(GuiGraphicsExtractor context,double mouseX, double mouseY){
                Render2D.drawBorderAroundText(context, this.x, this.y, this.width, this.height,2, ColorUtil.getPulsatingColor());
                context.text(mc.font, this.text, this.x, this.y, ColorUtil.getPulsatingColor(), true);
            }
        };

        closeButton = new GUIButton("X", this.x, this.y) {
            @Override
            public void draw(GuiGraphicsExtractor context,double mouseX, double mouseY){
                Render2D.drawBorderAroundText(context, this.x, this.y, this.width, this.height,2, ColorUtil.getPulsatingColor());
                context.text(mc.font, this.text, this.x, this.y, ColorUtil.getPulsatingColor(), true);
            }

            @Override
            public void click(double mouseX, double mouseY, int input) {
                close();
            }
        };

        this.dragArea = new Window(this.x, this.y, this.width, this.height);
    }

    @Override
    public void addDrawables() {
        titleText.x = this.x + this.width - titleText.width - 1;
        titleText.y = this.y - titleText.height - 1;

        closeButton.x = this.x + (closeButton.width  / 2);
        closeButton.y = this.y - closeButton.height - 1;

        dragArea.x = this.x;
        dragArea.y = closeButton.y - 3;
        dragArea.height = closeButton.height + 4;
        dragArea.width = this.width;

        this.add(dragArea);
        this.add(closeButton);
        this.add(titleText);
    }
}
