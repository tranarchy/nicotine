package nicotine.mod.mods.hud;

import net.minecraft.client.util.InputUtil;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.EventBus;
import nicotine.util.Keybind;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class HUDEditor {

    public static void init() {
        Mod hudEditor = new Mod("HUDEditor");
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_H);
        ToggleOption showPosition = new ToggleOption("ShowPosition");
        hudEditor.modOptions.addAll(Arrays.asList(showPosition, keybind));
        ModManager.addMod(ModCategory.HUD, hudEditor);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(hudEditor, keybind.keyCode))
                hudEditor.toggle();

            if (hudEditor.enabled) {
                mc.setScreen(new HUDEditorScreen(showPosition.enabled));
                hudEditor.toggle();
            }

            return true;
        });
    }
}
