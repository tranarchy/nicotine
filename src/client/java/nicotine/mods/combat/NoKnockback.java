package nicotine.mods.combat;

import nicotine.events.SetVelocityClientEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;

public class NoKnockback {
    public static void init() {
        Mod noKnockback = new Mod();
        noKnockback.name = "NoKnockback";
        modules.get(Category.Combat).add(noKnockback);

        EventBus.register(SetVelocityClientEvent.class, event  -> {
            return !noKnockback.enabled;
        });

    }
}
