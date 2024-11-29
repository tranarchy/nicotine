package nicotine.mod.mods.player;

import nicotine.events.GetVelocityMultiplierEvent;
import nicotine.events.IsUsingItemEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

public class NoSlow {
    public static void init() {
        Mod noSlow = new Mod("NoSlow");
        ModManager.addMod(ModCategory.Player, noSlow);

        EventBus.register(IsUsingItemEvent.class, event -> {
            if (!noSlow.enabled)
                return true;

            return false;
        });

        EventBus.register(GetVelocityMultiplierEvent.class, event -> {
            if (!noSlow.enabled)
                return true;

            return false;
        });
    }
}
