package nicotine.mod.mods.render;

import net.minecraft.client.render.BackgroundRenderer;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

public class NoFog {

    private static boolean fogEnabled = true;

    public static void init() {
        Mod noFog = new Mod();
        noFog.name = "NoFog";
        ModManager.modules.get(ModCategory.Render).add(noFog);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!noFog.enabled) {
                if (!fogEnabled)
                    fogEnabled = BackgroundRenderer.toggleFog();
                return true;
            }

            if (fogEnabled)
                fogEnabled = BackgroundRenderer.toggleFog();

            return true;
        });
    }
}
