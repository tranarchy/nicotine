package nicotine.mod.mods.render;

import nicotine.events.RenderWeatherEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

public class NoWeather {
    public static void init() {
        Mod noWeather = new Mod();
        noWeather.name = "NoWeather";
        ModManager.modules.get(ModCategory.Render).add(noWeather);

        EventBus.register(RenderWeatherEvent.class, event -> {
            return !noWeather.enabled;
        });
    }
}
