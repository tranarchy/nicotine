package nicotine.mod.mods.player;

import nicotine.events.CaughtFishEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoFish extends Mod {

    public AutoFish() {
        super(ModCategory.Player, "AutoFish");
    }

    @Override
    protected void init() {
        EventBus.register(CaughtFishEvent.class, event -> {
            if (!this.enabled)
                return true;

            mc.gameMode.useItem(mc.player, mc.player.getUsedItemHand());
            mc.gameMode.useItem(mc.player, mc.player.getUsedItemHand());

            return true;
        });

    }
}
