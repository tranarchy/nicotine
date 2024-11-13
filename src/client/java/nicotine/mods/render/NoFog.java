package nicotine.mods.render;

import net.minecraft.client.render.BackgroundRenderer;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;

public class NoFog {

    private static boolean fogEnabled = true;

    public static void init() {
        Mod noFog = new Mod();
        noFog.name = "NoFog";
        modules.get(Category.Render).add(noFog);

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
