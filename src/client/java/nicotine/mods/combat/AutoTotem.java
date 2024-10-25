package nicotine.mods.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

import static nicotine.util.Modules.*;

public class AutoTotem {

    public static void init() {
        Mod autoTotem = new Mod();
        autoTotem.name = "AutoTotem";
        modList.get("Combat").add(autoTotem);

        ClientTickEvents.START_CLIENT_TICK.register(client -> {

            if (!autoTotem.enabled || client.player == null || !client.player.getOffHandStack().isEmpty())
                return;

            for (int i = 9; i <= 35; i++) {
                if (client.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, client.player);
                    client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, client.player);
                    break;
                }
            }
        });

    }
}
