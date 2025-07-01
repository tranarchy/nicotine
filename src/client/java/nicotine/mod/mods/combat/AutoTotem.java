package nicotine.mod.mods.combat;

import net.minecraft.item.Items;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.Inventory;

import static nicotine.util.Common.mc;

public class AutoTotem {
    public static void init() {
        Mod autoTotem = new Mod("AutoTotem");
        ModManager.addMod(ModCategory.Combat, autoTotem);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoTotem.enabled || !mc.player.getOffHandStack().isEmpty() || Inventory.isContainerOpen())
                return true;

            for (int i = 0; i <= 35; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    Inventory.move(i < 9 ? 36 + i : i, 45);
                    break;
                }
            }

            return true;
        });
    }
}
