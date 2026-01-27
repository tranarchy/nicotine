package nicotine.screens.clickgui.element.button;

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
    public void click(double mouseX, double mouseY) {
        this.toggleOption.enabled = !this.toggleOption.enabled;
    }
}
