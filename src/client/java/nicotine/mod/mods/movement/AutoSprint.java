package nicotine.mod.mods.movement;

import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoSprint {
    public static void init() {
        Mod autoSprint = new Mod("AutoSprint") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!this.enabled) {
                    mc.options.sprintKey.setPressed(false);
                }
            }
        };
        ModManager.addMod(ModCategory.Movement, autoSprint);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoSprint.enabled)
                return true;

            mc.options.sprintKey.setPressed(true);

            return true;
        });
    }
}
