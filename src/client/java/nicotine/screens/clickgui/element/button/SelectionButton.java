package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import nicotine.mod.option.SelectionOption;
import nicotine.screens.clickgui.ItemSelectionScreen;

import static nicotine.util.Common.mc;

public class SelectionButton extends GUIButton {
    private final SelectionOption selectionOption;

    public SelectionButton(SelectionOption selectionOption, int x, int y) {
        super(selectionOption.name, x, y);
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
        this.selectionOption = selectionOption;
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        if (mc.level != null)
            mc.setScreen(new ItemSelectionScreen(selectionOption));
    }
}
