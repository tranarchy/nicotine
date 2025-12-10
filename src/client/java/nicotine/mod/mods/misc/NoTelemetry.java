package nicotine.mod.mods.misc;

import nicotine.events.AllowsTelemetryEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

public class NoTelemetry {
    public static void init() {
        Mod noTelemetry = new Mod("NoTelemetry", "Disables Microsoft telemetry");
        ModManager.addMod(ModCategory.Misc, noTelemetry);

        EventBus.register(AllowsTelemetryEvent.class, event -> {
            return !noTelemetry.enabled;
        });

    }
}
