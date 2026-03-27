package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.mod.option.SwitchOption;

import static nicotine.util.Common.*;

public class SwitchButton extends GUIButton {
    private final SwitchOption switchOption;

    public SwitchButton(SwitchOption switchOption, int x, int y) {
        super(String.format("%s [%s]", switchOption.name, switchOption.value), x, y);
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
        this.switchOption = switchOption;
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        for (int i = 0; i < switchOption.modes.length; i++) {
            if (switchOption.value.equals(switchOption.modes[i])) {
                switchOption.value = switchOption.modes[i+1 < switchOption.modes.length ? i+1 : 0];
                break;
            }
        }

        this.text = String.format("%s [%s]", switchOption.name, switchOption.value);
        this.width = mc.font.width(this.text);
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        super.draw(context, mouseX, mouseY);

        if (mouseOverButton(mouseX, mouseY)) {
            drawUnderline(context);
        }
    }
}
