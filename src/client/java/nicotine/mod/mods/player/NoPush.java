package nicotine.mod.mods.player;

import nicotine.events.PushEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

public class NoPush {

    public static void init() {
        Mod noPush = new Mod("NoPush", "Prevents entities from pushing you around");
        ModManager.addMod(ModCategory.Player, noPush);

        EventBus.register(PushEvent.class, event -> {
            if (!noPush.enabled)
                return true;

            return false;
        });
    }
}
