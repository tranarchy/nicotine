package nicotine.mod.mods.player;

import net.minecraft.world.item.ItemStack;
import nicotine.events.ClientLevelTickEvent;
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

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!autoRefill.enabled || mc.screen != null)
                return true;

            if (mc.player.getMainHandItem().getCount() == mc.player.getMainHandItem().getMaxStackSize() || mc.player.getMainHandItem() == ItemStack.EMPTY)
                return true;

            for (int i = 9; i <= 35; i++) {
                if (mc.player.getMainHandItem().getHoverName().getString().equals(mc.player.getInventory().getItem(i).getHoverName().getString())) {
                    Inventory.swap(i, 36 + mc.player.getInventory().getSelectedSlot());
                }
            }

            return true;
        });

    }
}
