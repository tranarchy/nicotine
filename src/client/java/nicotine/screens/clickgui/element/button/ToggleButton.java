package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.clickgui.element.misc.Text;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import static nicotine.util.Common.mc;

public class ToggleButton extends GUIButton {
    private final Text boxText;

    public final ToggleOption toggleOption;

    public ToggleButton(ToggleOption toggleOption, int x, int y) {
        super(toggleOption.name, x, y);
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
        this.toggleOption = toggleOption;

        this.boxText = new Text("✔", this.x + this.width + 4, this.y + 1);
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        this.toggleOption.toggle();
    }

    @Override
    public boolean mouseOverButton(double mouseX, double mouseY) {
        return Render2D.mouseOver(this.x, this.y, this.width + this.boxText.width + 4, this.height, mouseX, mouseY);
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        this.color = this.toggleOption.enabled ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR;
        this.boxText.color = this.color;

        Render2D.drawBorderAroundText(context, this.boxText.text, this.boxText.x, this.boxText.y, 1, ColorUtil.BORDER_COLOR);
        this.boxText.text = this.toggleOption.enabled ? "✔" : "";
        this.boxText.draw(context, mouseX, mouseY);

        super.draw(context, mouseX, mouseY);

        if (mouseOverButton(mouseX, mouseY)) {
            drawUnderline(context);
        }
    }
}
