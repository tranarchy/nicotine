package nicotine.events;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class AbstractContainerScreenTickEvent {
    public AbstractContainerScreen abstractContainerScreen;

    public AbstractContainerScreenTickEvent(AbstractContainerScreen abstractContainerScreen) {
        this.abstractContainerScreen = abstractContainerScreen;
    }
}
