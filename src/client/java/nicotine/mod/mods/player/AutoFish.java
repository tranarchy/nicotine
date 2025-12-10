package nicotine.mod.mods.player;

import nicotine.events.CaughtFishEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoFish {
    public static void init() {
        Mod autoFish = new Mod("AutoFish");
        ModManager.addMod(ModCategory.Player, autoFish);

        EventBus.register(CaughtFishEvent.class, event -> {
            if (!autoFish.enabled)
                return true;

            mc.gameMode.useItem(mc.player, mc.player.getUsedItemHand());
            mc.gameMode.useItem(mc.player, mc.player.getUsedItemHand());

            return true;
        });

    }
}
