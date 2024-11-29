package nicotine.mod.mods.misc;

import nicotine.events.DisconnectEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.screens.AutoReconnectScreen;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoReconnect {

    public static void init() {
        Mod autoReconnect = new Mod("AutoReconnect");
        SliderOption delay = new SliderOption(
                "Delay",
                20,
                1,
                60
        );
        autoReconnect.modOptions.add(delay);
        ModManager.addMod(ModCategory.Misc, autoReconnect);

        EventBus.register(DisconnectEvent.class, event -> {
            if (!autoReconnect.enabled)
                return true;

            mc.setScreen(new AutoReconnectScreen((int)delay.value * 20));
            return true;
        });

    }
}
