package nicotine.mod.mods.hud;

import com.mojang.blaze3d.platform.InputConstants;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.EventBus;
import nicotine.util.Keybind;

import static nicotine.util.Common.mc;

public class HUDEditor {

    public static void init() {
        Mod hudEditor = new Mod("HUDEditor");
        KeybindOption keybind = new KeybindOption(InputConstants.KEY_H);
        hudEditor.modOptions.add(keybind);
        ModManager.addMod(ModCategory.HUD, hudEditor);

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (Keybind.keyReleased(hudEditor, keybind.keyCode))
                hudEditor.toggle();

            if (hudEditor.enabled) {
                mc.setScreen(new HUDEditorScreen());
                hudEditor.toggle();
            }

            return true;
        });
    }
}
