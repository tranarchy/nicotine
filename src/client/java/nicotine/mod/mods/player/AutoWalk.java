package nicotine.mod.mods.player;

import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

import static nicotine.util.Common.*;

public class AutoWalk {

    public static void init() {
        Mod autoWalk = new Mod();
        autoWalk.name = "AutoWalk";
        ModManager.modules.get(ModCategory.Player).add(autoWalk);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoWalk.enabled)
                return true;

            mc.options.forwardKey.setPressed(true);
            return true;
        });
    }
}
