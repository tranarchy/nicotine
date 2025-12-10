package nicotine.mod.mods.combat;

import nicotine.events.ExplosionKnockbackEvent;
import nicotine.events.KnockbackEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class NoKnockback {
    public static void init() {
        Mod noKnockback = new Mod("NoKnockback", "Adjusts knockback from entities and explosions");
        SliderOption entity = new SliderOption("Entity", 0, 0, 1.0f, true);
        SliderOption explosion = new SliderOption("Explosion", 0, 0, 1.0f, true);
        noKnockback.modOptions.addAll(Arrays.asList(entity, explosion));
        ModManager.addMod(ModCategory.Combat, noKnockback);

        EventBus.register(KnockbackEvent.class, event -> {
            if (!noKnockback.enabled)
                return true;

            mc.player.push(event.x * entity.value, event.y * entity.value, event.z * entity.value);

            return false;
        });

        EventBus.register(ExplosionKnockbackEvent.class, event -> {
            if (!noKnockback.enabled)
                return true;

            mc.player.push(event.x * explosion.value, event.y * explosion.value, event.z * explosion.value);

            return false;
        });
    }
}
