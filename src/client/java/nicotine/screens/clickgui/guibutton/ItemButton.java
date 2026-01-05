package nicotine.screens.clickgui.guibutton;

import net.minecraft.world.item.ItemStack;

public class ItemButton extends GUIButton {
    public ItemStack itemStack;

    public ItemButton(int x, int y, int width, int height, ItemStack itemStack) {
        super(x, y, width, height);

        this.itemStack = itemStack;
    }
}
