package nicotine.mod.mods.hud;

import com.mojang.blaze3d.platform.InputConstants;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.KeybindOption;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.EventBus;
import nicotine.util.Keybind;

import static nicotine.util.Common.mc;

public class HUDEditor extends Mod {

    private final KeybindOption keybind = new KeybindOption(InputConstants.KEY_H);

    public HUDEditor() {
        super(ModCategory.HUD, "HUDEditor");
        this.modOptions.add(keybind);
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (Keybind.keyReleased(this, keybind.keyCode))
                this.toggle();

            if (this.enabled) {
                mc.setScreen(new HUDEditorScreen());
                this.toggle();
            }

            return true;
        });
    }
}
