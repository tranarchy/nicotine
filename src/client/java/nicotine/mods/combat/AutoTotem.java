package nicotine.mods.combat;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;
import static nicotine.util.Common.*;

public class AutoTotem {



    public static void init() {
        Mod autoTotem = new Mod();
        autoTotem.name = "AutoTotem";
        modules.get(Category.Combat).add(autoTotem);

        DefaultedList<ItemStack> prevInventory = DefaultedList.of();

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoTotem.enabled || !mc.player.getOffHandStack().isEmpty() || prevInventory.containsAll(mc.player.getInventory().main))
                return true;

            int syncId = mc.player.currentScreenHandler.syncId;

            for (int i = 9; i <= 35; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    mc.interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(syncId, 45, 0, SlotActionType.PICKUP, mc.player);
                    break;
                }
            }

            prevInventory.clear();
            prevInventory.addAll(mc.player.getInventory().main);

            return true;
        });
    }
}
