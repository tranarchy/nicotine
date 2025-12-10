package nicotine.screens;

import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;

import static nicotine.util.Common.*;

public class PeekScreen extends ContainerScreen {
    public PeekScreen(Component name, Container inventory) {
        super(ChestMenu.threeRows(-1, mc.player.getInventory(), inventory), mc.player.getInventory(), name);
    }
}
