package nicotine.mod.mods.player;

import net.minecraft.client.option.KeyBinding;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoMine {

    public static void init() {
        Mod autoMine = new Mod("AutoMine") {
            @Override
            public void toggle() {
                KeyBinding.unpressAll();
                this.enabled = !this.enabled;
            }
        };
        ModManager.addMod(ModCategory.Player, autoMine);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoMine.enabled)
                return true;

            mc.options.attackKey.setPressed(true);
            return true;
        });
    }
}
