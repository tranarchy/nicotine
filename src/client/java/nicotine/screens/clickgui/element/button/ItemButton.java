package nicotine.screens.clickgui.element.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nicotine.mod.option.SelectionOption;
import nicotine.util.ColorUtil;
import nicotine.util.render.GUI;

public class ItemButton extends GUIButton {
    public ItemStack itemStack;
    private final SelectionOption selectionOption;

    public ItemButton(ItemStack itemStack, SelectionOption selectionOption, int x, int y) {
        super(x, y);

        this.width = 16;
        this.height = 16;
        this.itemStack = itemStack;
        this.selectionOption = selectionOption;
    }

    @Override
    public void draw(GuiGraphics context, double mouseX, double mouseY) {
        if (selectionOption.items.contains(this.itemStack.getItem()))
            context.fill(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtil.FOREGROUND_COLOR);

        if (mouseOverButton(mouseX, mouseY))
            GUI.drawBorder(context, this.x, this.y, this.width, this.height, ColorUtil.ACTIVE_FOREGROUND_COLOR);

        context.renderFakeItem(this.itemStack, this.x, this.y);
    }

    @Override
    public void click(double mouseX, double mouseY) {
        Item item = this.itemStack.getItem();

        if (selectionOption.items.contains(item)) {
            selectionOption.items.remove(item);
        } else {
            selectionOption.items.add(item);
        }
    }
}
