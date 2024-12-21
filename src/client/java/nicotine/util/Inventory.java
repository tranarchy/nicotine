package nicotine.util;

import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.screen.slot.SlotActionType;

import static nicotine.util.Common.*;

public class Inventory {
    public static void move(int slot1, int slot2) {
        int syncId = mc.player.currentScreenHandler.syncId;

        mc.interactionManager.clickSlot(syncId, slot1, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(syncId, slot2, 0, SlotActionType.PICKUP, mc.player);
    }

    public static void swap(int slot1, int slot2) {
        int syncId = mc.player.currentScreenHandler.syncId;

        mc.interactionManager.clickSlot(syncId, slot1, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(syncId, slot2, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(syncId, slot1, 0, SlotActionType.PICKUP, mc.player);
    }

    public static void throwAway(int slot) {
        int syncId = mc.player.currentScreenHandler.syncId;

        mc.interactionManager.clickSlot(syncId, slot, 1, SlotActionType.THROW, mc.player);
    }

    public static boolean isContainerOpen() {
        if (mc.currentScreen instanceof GenericContainerScreen || mc.currentScreen instanceof ShulkerBoxScreen)
            return true;

        return false;
    }
}
