package nicotine.mod.mods.player;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import nicotine.events.PacketOutEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AlphaInventory {
    public static void init() {
        Mod alphaInventory = new Mod("AlphaInventory", "Let's you store items in the crafting grid");
        ModManager.addMod(ModCategory.Player, alphaInventory);

        EventBus.register(PacketOutEvent.class, event -> {
            if (!alphaInventory.enabled)
                return true;

            if (event.packet instanceof ServerboundContainerClosePacket && mc.screen instanceof InventoryScreen) {
                return false;
            }

            return true;
        });
    }
}
