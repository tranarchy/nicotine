package nicotine.mod.mods.player;

import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

import static nicotine.util.Common.*;

public class AutoMine {

    public static void init() {
        Mod autoMine = new Mod();
        autoMine.name = "AutoMine";
        ModManager.modules.get(ModCategory.Player).add(autoMine);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoMine.enabled)
                return true;

            mc.options.attackKey.setPressed(true);
            return true;
        });
    }
}
