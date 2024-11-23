package nicotine.mod.mods.gui;

import net.minecraft.client.util.InputUtil;
import nicotine.clickgui.GUI;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;

public class Options {
    public static void init() {
        Mod options = new Mod();
        options.name = "Options";
        options.alwaysEnabled = true;
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_RIGHT_SHIFT);
        options.modOptions.add(keybind);
        ModManager.modules.get(ModCategory.GUI).add(options);

        final GUI gui = new GUI();

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (InputUtil.isKeyPressed(windowHandle, keybind.keyCode) && mc.currentScreen == null)
                mc.setScreen(gui);

            return true;
        });
    }
}
