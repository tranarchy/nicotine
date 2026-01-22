package nicotine.mod.mods.misc;

import nicotine.events.CollectPlayerEntriesEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

public class ExtraTab extends Mod {

    public ExtraTab() {
        super(ModCategory.Misc, "ExtraTab", "Tab now lists all players");
    }

    @Override
    protected void init() {
        EventBus.register(CollectPlayerEntriesEvent.class, event -> {
            if (!this.enabled)
                return true;

            return false;
        });
    }
}
