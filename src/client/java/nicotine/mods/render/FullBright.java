package nicotine.mods.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import static nicotine.util.Modules.*;

public class FullBright {
    public static void init() {
        Mod fullBright = new Mod();
        fullBright.name = "FullBright";
        modList.get("Render").add(fullBright);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null)
                return;

            if (!fullBright.enabled) {
                if (client.player.hasStatusEffect(StatusEffects.NIGHT_VISION))
                    client.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
                return;
            }

            StatusEffectInstance nightVision = new StatusEffectInstance(StatusEffects.NIGHT_VISION, 240);
            client.player.addStatusEffect(nightVision);
        });
    }
}
