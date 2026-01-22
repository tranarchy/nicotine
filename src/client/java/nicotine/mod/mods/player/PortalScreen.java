package nicotine.mod.mods.player;

import nicotine.events.InPortalEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

public class PortalScreen extends Mod {

    public PortalScreen() {
        super(ModCategory.Player, "PortalScreen");
    }

    @Override
    protected void init() {
        EventBus.register(InPortalEvent.class, event -> {
            if (!this.enabled)
                return true;

            return false;

        });
    }
}
