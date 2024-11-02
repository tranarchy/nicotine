package nicotine.mods.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BackgroundRenderer;

import static nicotine.util.Modules.*;

public class NoFog {

    private static boolean fogEnabled = true;

    public static void init() {
        Mod noFog = new Mod();
        noFog.name = "NoFog";
        modList.get("Render").add(noFog);

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if (!noFog.enabled) {
                if (!fogEnabled)
                    fogEnabled = BackgroundRenderer.toggleFog();
                return;
            }

            if (fogEnabled)
                fogEnabled = BackgroundRenderer.toggleFog();
        });
    }
}
