package nicotine.util;

import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.world.inventory.ClickType;
import nicotine.mixininterfaces.IMultiPlayerGameMode;

import static nicotine.util.Common.*;

public class Inventory {
    public static void selectSlot(int slot) {
        mc.player.getInventory().setSelectedSlot(slot);

        ((IMultiPlayerGameMode) mc.gameMode).syncSlot();
    }

    public static void move(int slot1, int slot2) {
        int syncId = mc.player.containerMenu.containerId;

        mc.gameMode.handleInventoryMouseClick(syncId, slot1, 0, ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(syncId, slot2, 0, ClickType.PICKUP, mc.player);

        ((IMultiPlayerGameMode) mc.gameMode).syncSlot();
    }

    public static void swap(int slot1, int slot2) {
        int syncId = mc.player.containerMenu.containerId;

        mc.gameMode.handleInventoryMouseClick(syncId, slot1, 0, ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(syncId, slot2, 0, ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(syncId, slot1, 0, ClickType.PICKUP, mc.player);

        ((IMultiPlayerGameMode) mc.gameMode).syncSlot();
    }

    public static void throwAway(int slot) {
        int syncId = mc.player.containerMenu.containerId;

        mc.gameMode.handleInventoryMouseClick(syncId, slot, 1, ClickType.THROW, mc.player);

        ((IMultiPlayerGameMode) mc.gameMode).syncSlot();
    }

    public static boolean isContainerOpen() {
        if (mc.screen instanceof ContainerScreen || mc.screen instanceof ShulkerBoxScreen)
            return true;

        return false;
    }
}
