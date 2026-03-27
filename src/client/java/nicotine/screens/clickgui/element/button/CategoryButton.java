package nicotine.screens.clickgui.element.button;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.mod.ModCategory;
import nicotine.util.ColorUtil;

import static nicotine.util.Common.mc;

public class CategoryButton extends GUIButton {
    public ModCategory modCategory;
    public static ModCategory selectedModCategory;

    public CategoryButton(ModCategory modCategory, int x, int y) {
        super(x, y);
        this.text = modCategory.name();
        this.modCategory = modCategory;
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
    }

    @Override
    public void click(double mouseX, double mouseY) {
        selectedModCategory = this.modCategory;
        ModButton.selectedMod = null;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        this.color = mouseOverButton(mouseX, mouseY) || this.selectedModCategory == this.modCategory ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR;

        context.text(mc.font, this.text, this.x, this.y, this.color, true);
    }
}
