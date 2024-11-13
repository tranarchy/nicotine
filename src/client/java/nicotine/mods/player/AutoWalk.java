package nicotine.mods.player;

import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;
import static nicotine.util.Common.mc;

public class AutoWalk {

    public static void init() {
        Mod autoWalk = new Mod();
        autoWalk.name = "AutoWalk";
        modules.get(Category.Player).add(autoWalk);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoWalk.enabled)
                return true;

            mc.options.forwardKey.setPressed(true);
            return true;
        });
    }
}
