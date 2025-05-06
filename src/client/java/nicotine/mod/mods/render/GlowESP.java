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
    public static void init() {
        Mod glowESP = new Mod("GlowESP", "Applies the glowing status effect to players\nNOTE: It can have a big performance hit on older computers");
        RGBOption rgb = new RGBOption();
        glowESP.modOptions.addAll(Arrays.asList(rgb.red, rgb.green, rgb.blue, rgb.rainbow));
        ModManager.addMod(ModCategory.Render, glowESP);

        EventBus.register(RenderEntityOutlineEvent.class, event -> {
            if (!glowESP.enabled)
                return true;

            Color color = new Color(rgb.getColor());
            event.outlineVertexConsumerProvider.setColor(color.getRed(), color.getGreen(), color.getBlue(), 255);

            return true;
        });
    }
}
