package nicotine.mod.mods.render;

import nicotine.events.RenderEntityOutlineEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.RGBOption;
import nicotine.util.EventBus;

import java.awt.*;
import java.util.Arrays;

public class GlowESP {
    public static Mod glowESP;

    public static void init() {
        glowESP = new Mod("GlowESP", "Applies the glowing status effect to players");
        RGBOption rgb = new RGBOption();
        glowESP.modOptions.addAll(Arrays.asList(rgb.red, rgb.green, rgb.blue, rgb.rainbow));
        ModManager.addMod(ModCategory.Render, glowESP);

        EventBus.register(RenderEntityOutlineEvent.class, event -> {
            if (!glowESP.enabled)
                return true;

            event.outlineVertexConsumerProvider.setColor(rgb.getColor());

            return true;
        });
    }
}
