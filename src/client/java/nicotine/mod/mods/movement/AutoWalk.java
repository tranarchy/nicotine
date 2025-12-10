package nicotine.mod.mods.movement;

import com.mojang.blaze3d.platform.InputConstants;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoWalk {

    public static void init() {
        Mod autoWalk = new Mod("AutoWalk") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!this.enabled) {
                    mc.options.keyUp.setDown(false);
                }
            }
        };
        KeybindOption keybind = new KeybindOption(InputConstants.KEY_U);
        autoWalk.modOptions.add(keybind);
        ModManager.addMod(ModCategory.Movement, autoWalk);

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!autoWalk.enabled)
                return true;

            mc.options.keyUp.setDown(true);
            return true;
        });
    }
}
