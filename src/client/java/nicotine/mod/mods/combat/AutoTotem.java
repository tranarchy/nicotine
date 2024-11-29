package nicotine.mod.mods.combat;

import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoTotem {
    public static void init() {
        Mod autoTotem = new Mod("AutoTotem");
        ModManager.addMod(ModCategory.Combat, autoTotem);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoTotem.enabled || !mc.player.getOffHandStack().isEmpty())
                return true;

            int syncId = mc.player.currentScreenHandler.syncId;

            for (int i = 9; i <= 35; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    mc.interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(syncId, 45, 0, SlotActionType.PICKUP, mc.player);
                    break;
                }
            }

            return true;
        });
    }
}
