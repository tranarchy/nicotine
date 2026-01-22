package nicotine.mod.mods.misc;

import nicotine.events.AllowsTelemetryEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

public class NoTelemetry extends Mod {

    public NoTelemetry() {
        super(ModCategory.Misc, "NoTelemetry", "Disables Microsoft telemetry");
    }

    @Override
    protected void init() {
        EventBus.register(AllowsTelemetryEvent.class, event -> {
            return !this.enabled;
        });

    }
}
