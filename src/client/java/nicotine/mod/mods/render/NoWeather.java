package nicotine.mod.mods.render;

import nicotine.events.RenderWeatherEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

public class NoWeather {
    public static void init() {
        Mod noWeather = new Mod("NoWeather");
        ModManager.addMod(ModCategory.Render, noWeather);

        EventBus.register(RenderWeatherEvent.class, event -> {
            return !noWeather.enabled;
        });
    }
}
