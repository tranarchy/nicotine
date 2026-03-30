package nicotine.screens.clickgui.element.button;

import nicotine.mod.option.ItemSelectionOption;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.screens.clickgui.element.window.subwindow.ItemSelectionWindow;

import static nicotine.util.Common.mc;

public class ItemSelectionButton extends SubWindowButton {
    private final ItemSelectionOption itemSelectionOption;

    public ItemSelectionButton(BaseScreen screen, String title, ItemSelectionOption itemSelectionOption, int x, int y) {
        super(screen, itemSelectionOption.name, title, x, y);
        this.itemSelectionOption = itemSelectionOption;
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (mc.level != null || ItemSelectionWindow.builtContents) {
           this.setSubWindow(new ItemSelectionWindow(this.screen, this.title, itemSelectionOption));
           super.click(mouseX, mouseY, input);
        }
    }
}
