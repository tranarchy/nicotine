package nicotine.mod.mods.combat;

import net.minecraft.util.math.Vec3d;
import nicotine.events.SetVelocityClientEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

import static nicotine.util.Common.*;

public class NoKnockback {
    public static void init() {
        Mod noKnockback = new Mod();
        noKnockback.name = "NoKnockback";
        ModManager.modules.get(ModCategory.Combat).add(noKnockback);

        EventBus.register(SetVelocityClientEvent.class, event  -> {
            if (!noKnockback.enabled)
                return true;

            return false;
        });

    }
}
