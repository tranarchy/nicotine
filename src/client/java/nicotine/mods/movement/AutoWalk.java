package nicotine.mods.movement;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import static nicotine.util.Modules.*;
import static nicotine.util.Common.*;

public class AutoWalk {
    public static void init() {
        Mod autoWalk = new Mod();
        autoWalk.name = "AutoWalk";
        modList.get("Movement").add(autoWalk);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!autoWalk.enabled || client.player == null )
                return;

            minecraftClient.options.forwardKey.setPressed(true);
        });
    }
}
