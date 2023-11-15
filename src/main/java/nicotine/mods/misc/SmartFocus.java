package nicotine.mods.misc;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import nicotine.util.Common;
import nicotine.util.Module;

public class SmartFocus {

    private static int defaultFps;
    private static boolean defaultVsync;

    public static void init() {
        Module.Mod smartFocus = new Module.Mod();
        smartFocus.name = "SmartFocus";
        Module.modList.get("Misc").add(smartFocus);

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!smartFocus.enabled || client.player == null ) {
                defaultFps = Common.mc.options.getMaxFps().getValue();
                defaultVsync = Common.mc.options.getEnableVsync().getValue();
                return;
            }

           if (Common.mc.isWindowFocused()) {
               Common.mc.options.getMaxFps().setValue(defaultFps);
               Common.mc.options.getEnableVsync().setValue(defaultVsync);
           } else {
               Common.mc.options.getMaxFps().setValue(10);
               Common.mc.options.getEnableVsync().setValue(false);
           }
        });
    }
}
