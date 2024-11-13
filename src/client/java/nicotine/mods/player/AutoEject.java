package nicotine.mods.player;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;

import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;
import static nicotine.util.Modules.*;

public class AutoEject {
    public static void init() {
        Mod autoEject = new Mod();
        autoEject.name = "AutoEject";
        modules.get(Category.Player).add(autoEject);

        final List<Item> junkItems = Arrays.asList(
                Items.DIRT,
                Items.COBBLESTONE,
                Items.COBBLED_DEEPSLATE,
                Items.NETHERRACK,
                Items.ANDESITE,
                Items.DIORITE,
                Items.GRANITE,
                Items.ROTTEN_FLESH
        );

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoEject.enabled || mc.currentScreen != null)
                return true;

            int syncId = mc.player.currentScreenHandler.syncId;

            for (int i = 9; i <= 35; i++) {
                if (junkItems.contains(mc.player.getInventory().getStack(i).getItem())) {
                    mc.interactionManager.clickSlot(syncId, i, 0, SlotActionType.THROW, mc.player);
                }
            }

            return true;
        });

    }
}
