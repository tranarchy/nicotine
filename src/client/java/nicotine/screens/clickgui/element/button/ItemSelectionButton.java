package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.mod.mods.general.GUI;
import nicotine.mod.option.ItemSelectionOption;
import nicotine.screens.clickgui.ItemSelectionScreen;

import static nicotine.util.Common.mc;

public class ItemSelectionButton extends GUIButton {
    private final ItemSelectionOption selectionOption;
    private final String title;

    public ItemSelectionButton(String title, ItemSelectionOption selectionOption, int x, int y) {
        super(selectionOption.name, x, y);
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
        this.selectionOption = selectionOption;
        this.title = title;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        super.draw(context, mouseX, mouseY);

        if (mouseOverElement(mouseX, mouseY)) {
            drawUnderline(context);
        }
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        if (mc.level != null || ItemSelectionScreen.builtContents) {
            ItemSelectionScreen itemSelectionScreen = new ItemSelectionScreen(GUI.screen, title, selectionOption);
            itemSelectionScreen.x = (int)mouseX;
            itemSelectionScreen.y = (int)mouseY;

            GUI.screen.addSubWindow(itemSelectionScreen);
        }
    }
}
