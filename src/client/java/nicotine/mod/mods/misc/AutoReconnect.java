package nicotine.mod.mods.misc;

import nicotine.events.DisconnectEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.screens.AutoReconnectScreen;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

import static nicotine.util.Common.*;

public class AutoReconnect {

    public static void init() {
        Mod autoReconnect = new Mod();
        autoReconnect.name = "AutoReconnect";
        SliderOption delay = new SliderOption(
                "Delay",
                20,
                1,
                60
        );
        autoReconnect.modOptions.add(delay);
        ModManager.modules.get(ModCategory.Misc).add(autoReconnect);

        EventBus.register(DisconnectEvent.class, event -> {
            if (!autoReconnect.enabled)
                return true;

            mc.setScreen(new AutoReconnectScreen((int)delay.value * 20));
            return true;
        });

    }
}
