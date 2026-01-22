package nicotine.mod.mods.combat;

import nicotine.events.ExplosionKnockbackEvent;
import nicotine.events.KnockbackEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class NoKnockback extends Mod {

    private final SliderOption entity = new SliderOption(
            "Entity",
            0,
            0,
            1.0f,
            true
    );

    private final SliderOption explosion = new SliderOption(
            "Explosion",
            0,
            0,
            1.0f,
            true
    );

    public NoKnockback() {
        super(ModCategory.Combat, "NoKnockback", "Adjusts knockback from entities and explosions");
        this.modOptions.addAll(Arrays.asList(entity, explosion));
    }

    @Override
    protected void init() {
        EventBus.register(KnockbackEvent.class, event -> {
            if (!this.enabled)
                return true;

            mc.player.push(event.x * entity.value, event.y * entity.value, event.z * entity.value);

            return false;
        });

        EventBus.register(ExplosionKnockbackEvent.class, event -> {
            if (!this.enabled)
                return true;

            mc.player.push(event.x * explosion.value, event.y * explosion.value, event.z * explosion.value);

            return false;
        });
    }
}
