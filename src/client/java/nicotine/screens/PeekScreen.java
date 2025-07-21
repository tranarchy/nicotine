package nicotine.screens;

import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;

import static nicotine.util.Common.*;

public class PeekScreen extends GenericContainerScreen {
    public PeekScreen(Text name, Inventory inventory) {
        super(GenericContainerScreenHandler.createGeneric9x3(-1, mc.player.getInventory(), inventory), mc.player.getInventory(), name);
    }
}
