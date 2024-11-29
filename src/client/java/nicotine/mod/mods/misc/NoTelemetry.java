package nicotine.mod.mods.misc;

import nicotine.events.IsTelemetryEnabledByApiEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

public class NoTelemetry { ;
    public static void init() {
        Mod noTelemetry = new Mod("NoTelemetry");
        ModManager.addMod(ModCategory.Misc, noTelemetry);

        EventBus.register(IsTelemetryEnabledByApiEvent.class, event -> {
            return !noTelemetry.enabled;
        });

    }
}
