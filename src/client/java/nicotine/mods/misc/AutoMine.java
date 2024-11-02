package nicotine.mods.misc;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import static nicotine.util.Common.minecraftClient;
import static nicotine.util.Modules.*;

public class AutoMine {
    public static void init() {
        Mod autoMine = new Mod();
        autoMine.name = "AutoMine";
        modList.get("Misc").add(autoMine);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!autoMine.enabled || client.player == null )
                return;

            minecraftClient.options.attackKey.setPressed(true);
        });
    }
}
