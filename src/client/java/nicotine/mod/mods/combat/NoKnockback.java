package nicotine.mod.mods.combat;

import nicotine.events.KnockbackEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;

public class NoKnockback {
    public static void init() {
        Mod noKnockback = new Mod("NoKnockback", "Disables knockback from entities and explosions");
        SliderOption strength = new SliderOption("Strength", 0, 0, 1.0f, true);
        noKnockback.modOptions.add(strength);
        ModManager.addMod(ModCategory.Combat, noKnockback);

        EventBus.register(KnockbackEvent.class, event -> {
            if (!noKnockback.enabled)
                return true;

            mc.player.addVelocity(event.x * strength.value, event.y * strength.value, event.z * strength.value);

            return false;
        });
    }
}
