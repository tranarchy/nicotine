package nicotine.mod.mods.player;

import nicotine.events.PushEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

public class NoPush extends Mod {

    public NoPush() {
        super(ModCategory.Player, "NoPush", "Prevents entities from pushing you around");
    }

    @Override
    protected void init() {
        EventBus.register(PushEvent.class, event -> {
            if (!this.enabled)
                return true;

            return false;
        });
    }
}
