package nicotine.mod.mods.player;

import nicotine.events.InPortalEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

public class PortalScreen {
    public static void init() {
        Mod portalScreen = new Mod("PortalScreen");
        ModManager.addMod(ModCategory.Player, portalScreen);

        EventBus.register(InPortalEvent.class, event -> {
            if (!portalScreen.enabled)
                return true;

            return false;

        });
    }
}
