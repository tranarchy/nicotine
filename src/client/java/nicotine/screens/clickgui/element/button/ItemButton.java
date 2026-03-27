package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nicotine.mod.option.SelectionOption;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import static nicotine.util.Common.mc;

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
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        if (selectionOption.items.contains(this.itemStack.getItem()))
            context.fill(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtil.changeAlpha(ColorUtil.ACTIVE_FOREGROUND_COLOR, 128));

        if (mouseOverButton(mouseX, mouseY))
            Render2D.drawBorder(context, this.x, this.y, this.width, this.height, ColorUtil.ACTIVE_FOREGROUND_COLOR);

        context.fakeItem(this.itemStack, this.x, this.y);

        if (mouseOverButton(mouseX, mouseY)) {
            context.setComponentTooltipForNextFrame(mc.font, itemStack.getItemName().toFlatList(), (int) mouseX + 3, (int) mouseY + 3);
        }
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        Item item = this.itemStack.getItem();

        if (selectionOption.items.contains(item)) {
            selectionOption.items.remove(item);
        } else {
            selectionOption.items.add(item);
        }
    }
}
