package nicotine.mod.mods.combat;

import nicotine.events.SetVelocityClientEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

public class NoKnockback {
    public static void init() {
        Mod noKnockback = new Mod("NoKnockback");
        ModManager.addMod(ModCategory.Combat, noKnockback);

        EventBus.register(SetVelocityClientEvent.class, event  -> {
            if (!noKnockback.enabled)
                return true;

            return false;
        });

    }
}
