package nicotine.mod.mods.gui;

import net.minecraft.client.util.InputUtil;
import nicotine.clickgui.ClickGUI;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.RGBOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.windowHandle;

public class GUI {
    public static void init() {
        Mod gui = new Mod("GUI");
        gui.alwaysEnabled = true;
        RGBOption rgb = new RGBOption();
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_RIGHT_SHIFT);
        gui.modOptions.addAll(Arrays.asList(rgb.red, rgb.green, rgb.blue, rgb.rainbow, keybind));
        ModManager.addMod(ModCategory.GUI, gui);

        final ClickGUI clickGUI = new ClickGUI();

        EventBus.register(ClientWorldTickEvent.class, event -> {
            ColorUtil.ACTIVE_FOREGROUND_COLOR = rgb.getColor();

            if (InputUtil.isKeyPressed(windowHandle, keybind.keyCode) && mc.currentScreen == null)
                mc.setScreen(clickGUI);

            return true;
        });
    }
}
