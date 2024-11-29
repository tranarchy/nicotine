package nicotine.mod.mods.gui;

import net.minecraft.client.util.InputUtil;
import nicotine.clickgui.GUI;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;

import java.awt.*;
import java.util.Arrays;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.windowHandle;

public class Options {
    public static void init() {
        Mod options = new Mod("Options");
        options.alwaysEnabled = true;
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_RIGHT_SHIFT);
        SliderOption red = new SliderOption(
                "Red",
                95,
                0,
                255
        );
        SliderOption green = new SliderOption(
                "Green",
                68,
                0,
                255
        );
        SliderOption blue = new SliderOption(
                "Blue",
                196,
                0,
                255
        );
        options.modOptions.addAll(Arrays.asList(keybind, red, green, blue));
        ModManager.addMod(ModCategory.GUI, options);

        final GUI gui = new GUI();

        EventBus.register(ClientWorldTickEvent.class, event -> {
            int colorVal = new Color(red.value / 255, green.value / 255, blue.value / 255).getRGB();

            Colors.ACTIVE_FOREGROUND_COLOR = colorVal;
            Colors.CATEGORY_BACKGROUND_COLOR = colorVal;

            if (InputUtil.isKeyPressed(windowHandle, keybind.keyCode) && mc.currentScreen == null)
                mc.setScreen(gui);

            return true;
        });
    }
}
