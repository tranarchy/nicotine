package nicotine.mods.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import nicotine.util.Common;
import nicotine.util.Module;

public class Fullbright {

    public static void init() {
        Module.Mod fullbright = new Module.Mod();
        fullbright.name = "Fullbright";
        Module.modList.get("Render").add(fullbright);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!fullbright.enabled || Common.mc.player == null) {
                if (Common.mc.player != null) {
                    if (Common.mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                        Common.mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
                    }
                }
                return;
            }

            StatusEffectInstance nightVision = new StatusEffectInstance(StatusEffects.NIGHT_VISION, 240);
            Common.mc.player.addStatusEffect(nightVision);
        });
    }
}
