package nicotine.mod.mods.movement;

import net.minecraft.client.util.InputUtil;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.util.EventBus;
import nicotine.util.Keybind;

import static nicotine.util.Common.mc;

public class AutoWalk {

    public static void init() {
        Mod autoWalk = new Mod("AutoWalk") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!this.enabled) {
                    mc.options.forwardKey.setPressed(false);
                }
            }
        };
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_U);
        autoWalk.modOptions.add(keybind);
        ModManager.addMod(ModCategory.Movement, autoWalk);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(autoWalk, keybind.keyCode))
                autoWalk.toggle();

            if (!autoWalk.enabled)
                return true;

            mc.options.forwardKey.setPressed(true);
            return true;
        });
    }
}
