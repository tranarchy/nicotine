package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.mod.option.ToggleOption;
import nicotine.util.ColorUtil;

import static nicotine.util.Common.mc;

public class ToggleButton extends GUIButton {
    public final ToggleOption toggleOption;

    public ToggleButton(ToggleOption toggleOption, int x, int y) {
        super(toggleOption.name, x, y);
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
        this.toggleOption = toggleOption;
        this.color = this.toggleOption.enabled ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR;
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        this.toggleOption.toggle();
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        super.draw(context, mouseX, mouseY);

        if (mouseOverButton(mouseX, mouseY)) {
            drawUnderline(context);
        }
    }
}
