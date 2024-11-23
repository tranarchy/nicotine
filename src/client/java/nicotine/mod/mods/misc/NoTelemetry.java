package nicotine.mod.mods.misc;

import nicotine.events.IsTelemetryEnabledByApiEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

public class NoTelemetry { ;
    public static void init() {
        Mod noTelemetry = new Mod();
        noTelemetry.name = "NoTelemetry";
        ModManager.modules.get(ModCategory.Misc).add(noTelemetry);

        EventBus.register(IsTelemetryEnabledByApiEvent.class, event -> {
            return !noTelemetry.enabled;
        });

    }
}
