package nicotine.mod.mods.misc;

import nicotine.events.CollectPlayerEntriesEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

public class ExtraTab {
    public static void init() {
        Mod extraTab = new Mod();
        extraTab.name = "ExtraTab";
        ModManager.modules.get(ModCategory.Misc).add(extraTab);

        EventBus.register(CollectPlayerEntriesEvent.class, event -> {
            if (!extraTab.enabled)
                return true;

            return false;
        });
    }
}
