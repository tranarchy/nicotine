package nicotine.mods.misc;

import nicotine.events.IsTelemetryEnabledByApiEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;

public class NoTelemetry { ;
    public static void init() {
        Mod noTelemetry = new Mod();
        noTelemetry.name = "NoTelemetry";
        modules.get(Category.Misc).add(noTelemetry);

        EventBus.register(IsTelemetryEnabledByApiEvent.class, event -> {
            return !noTelemetry.enabled;
        });

    }
}
