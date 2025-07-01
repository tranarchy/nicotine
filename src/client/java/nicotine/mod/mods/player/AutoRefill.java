package nicotine.mod.mods.player;

import net.minecraft.item.ItemStack;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.Inventory;

import static nicotine.util.Common.mc;

public class AutoRefill {
    public static void init() {
        Mod autoRefill = new Mod("AutoRefill", "Refills the stack in your hand from your inventory");
        ModManager.addMod(ModCategory.Player, autoRefill);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoRefill.enabled || mc.currentScreen != null)
                return true;

            if (mc.player.getMainHandStack().getCount() == mc.player.getMainHandStack().getMaxCount() || mc.player.getMainHandStack() == ItemStack.EMPTY)
                return true;

            for (int i = 9; i <= 35; i++) {
                if (mc.player.getMainHandStack().getName().getString().equals(mc.player.getInventory().getStack(i).getName().getString())) {
                    Inventory.swap(i, 36 + mc.player.getInventory().getSelectedSlot());
                }
            }

            return true;
        });

    }
}
