package nicotine.mods.player;

import nicotine.events.GetVelocityMultiplierEvent;
import nicotine.events.IsUsingItemEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;

public class NoSlow {
    public static void init() {
        Mod noSlow = new Mod();
        noSlow.name = "NoSlow";
        modules.get(Category.Player).add(noSlow);

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
