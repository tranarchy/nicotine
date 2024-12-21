package nicotine.mod.mods.render;

import nicotine.events.RenderEntityOutlineEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.RGBOption;
import nicotine.util.EventBus;

import java.util.Arrays;

public class GlowESP {
    public static void init() {
        Mod glowESP = new Mod("GlowESP", "Applies the glowing status effect to players\nNOTE: Can have a big performance hit on older computers");
        RGBOption rgb = new RGBOption();
        glowESP.modOptions.addAll(Arrays.asList(rgb.red, rgb.green, rgb.blue));
        ModManager.addMod(ModCategory.Render, glowESP);


        EventBus.register(RenderEntityOutlineEvent.class, event -> {
            if (!glowESP.enabled)
                return true;

            event.outlineVertexConsumerProvider.setColor((int)rgb.red.value, (int)rgb.green.value, (int)rgb.blue.value, 255);

            return true;
        });
    }
}
