package nicotine.mod.mods.player;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.Inventory;

import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class AutoEject {
    public static void init() {
        Mod autoEject = new Mod("AutoEject", "Throws away junk items from your inventory");
        ModManager.addMod(ModCategory.Player, autoEject);

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
            if (!autoEject.enabled)
                return true;

            for (int i = 0; i <= 35; i++) {
                if (junkItems.contains(mc.player.getInventory().getStack(i).getItem())) {
                    Inventory.throwAway(i < 9 ? 36 + i : i);
                }
            }

            return true;
        });

    }
}
