package nicotine.mod.mods.player;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import nicotine.events.ClientLevelTickEvent;
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

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!autoEject.enabled)
                return true;

            for (int i = 0; i <= 35; i++) {
                if (junkItems.contains(mc.player.getInventory().getItem(i).getItem())) {
                    Inventory.throwAway(i < 9 ? 36 + i : i);
                }
            }

            return true;
        });

    }
}
