package nicotine.mod.mods.player;

import net.minecraft.world.item.Items;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SelectionOption;
import nicotine.util.EventBus;
import nicotine.util.Inventory;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class AutoEject extends Mod {

    private final SelectionOption junk = new SelectionOption("Items");

    public AutoEject() {
        super(ModCategory.Player, "AutoEject", "Throws away junk items from your inventory");

        junk.items.addAll(Arrays.asList(
                Items.DIRT,
                Items.COBBLESTONE,
                Items.COBBLED_DEEPSLATE,
                Items.NETHERRACK,
                Items.ANDESITE,
                Items.DIORITE,
                Items.GRANITE,
                Items.ROTTEN_FLESH
        ));

        this.modOptions.add(junk);
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            for (int i = 0; i <= 35; i++) {
                if (junk.items.contains(mc.player.getInventory().getItem(i).getItem())) {
                    Inventory.throwAway(i < 9 ? 36 + i : i);
                }
            }

            return true;
        });

    }
}
