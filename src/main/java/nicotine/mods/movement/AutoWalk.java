package nicotine.mods.movement;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import nicotine.util.Module;

import static nicotine.util.Common.mc;

public class AutoWalk {
    public static void init() {
        Module.Mod autoWalk = new Module.Mod();
        autoWalk.name = "AutoWalk";
        Module.modList.get("Movement").add(autoWalk);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!autoWalk.enabled || client.player == null )
                return;

            mc.options.forwardKey.setPressed(true);
        });
    }
}
