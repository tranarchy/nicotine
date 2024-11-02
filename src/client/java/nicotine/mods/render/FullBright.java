package nicotine.mods.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.world.chunk.light.LightingProvider;
import org.lwjgl.glfw.GLFW;

import static nicotine.util.Common.*;
import static nicotine.util.Modules.*;

public class FullBright {
    public static void init() {
        Mod fullBright = new Mod();
        fullBright.name = "FullBright";
        modList.get("Render").add(fullBright);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null)
                return;

            SimpleOption<Double> gammaOption = minecraftClient.options.getGamma();

            if (!fullBright.enabled) {
                if (gammaOption.getValue() > 1.0)
                    gammaOption.setValue(1.0);

                return;
            }

            gammaOption.setValue(1000000.0);
        });
    }
}
