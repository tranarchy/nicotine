package nicotine.mod.mods.player;

import net.minecraft.client.option.KeyBinding;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoWalk {

    public static void init() {
        Mod autoWalk = new Mod("AutoWalk") {
            @Override
            public void toggle() {
                KeyBinding.unpressAll();
                this.enabled = !this.enabled;
            }
        };
        ModManager.addMod(ModCategory.Player, autoWalk);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoWalk.enabled)
                return true;

            mc.options.forwardKey.setPressed(true);
            return true;
        });
    }
}
