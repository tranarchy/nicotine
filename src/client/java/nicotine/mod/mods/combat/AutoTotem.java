package nicotine.mod.mods.combat;

import net.minecraft.world.item.Items;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;
import nicotine.util.Inventory;


import static nicotine.util.Common.mc;

public class AutoTotem extends Mod {

    public AutoTotem() {
        super(ModCategory.Combat,"AutoTotem");
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled || !mc.player.getOffhandItem().isEmpty() || Inventory.isContainerOpen())
                return true;

            for (int i = 0; i <= 35; i++) {
                if (mc.player.getInventory().getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    Inventory.move(i < 9 ? 36 + i : i, 45);
                    break;
                }
            }

            return true;
        });
    }
}
