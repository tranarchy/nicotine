package nicotine.mods.render;

import nicotine.events.RenderWeatherEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;

public class NoWeather {
    public static void init() {
        Mod noWeather = new Mod();
        noWeather.name = "NoWeather";
        modules.get(Category.Render).add(noWeather);

        EventBus.register(RenderWeatherEvent.class, event -> {
            return !noWeather.enabled;
        });
    }
}
