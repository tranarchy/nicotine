package nicotine.mods.player;


import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;
import static nicotine.util.Modules.*;

public class AutoMine {

    public static void init() {
        Mod autoMine = new Mod();
        autoMine.name = "AutoMine";
        modules.get(Category.Player).add(autoMine);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoMine.enabled)
                return true;

            mc.options.attackKey.setPressed(true);
            return true;
        });
    }
}
