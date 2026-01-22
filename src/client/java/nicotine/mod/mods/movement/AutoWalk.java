package nicotine.mod.mods.movement;

import com.mojang.blaze3d.platform.InputConstants;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.KeybindOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoWalk extends Mod {

    private final KeybindOption keybind = new KeybindOption(InputConstants.KEY_U);

    public AutoWalk() {
        super(ModCategory.Movement, "AutoWalk");
        this.modOptions.add(keybind);
    }

    @Override
    public void toggle() {
        this.enabled = !this.enabled;

        if (!this.enabled) {
            mc.options.keyUp.setDown(false);
        }
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            mc.options.keyUp.setDown(true);
            return true;
        });
    }
}
