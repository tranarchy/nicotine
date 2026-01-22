package nicotine.mod.mods.movement;

import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoSprint extends Mod {

    public AutoSprint() {
        super(ModCategory.Movement, "AutoSprint");
    }

    @Override
    public void toggle() {
        this.enabled = !this.enabled;

        if (!this.enabled) {
            mc.options.keySprint.setDown(false);
        }
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            mc.options.keySprint.setDown(true);

            return true;
        });
    }
}
