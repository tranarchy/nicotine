package nicotine.mod.mods.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.TitleScreen;
import nicotine.screens.clickgui.ClickGUI;
import nicotine.events.ClientTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.RGBOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class GUI {
    public static void init() {
        Mod gui = new Mod("GUI");
        gui.alwaysEnabled = true;
        RGBOption rgb = new RGBOption();
        KeybindOption keybind = new KeybindOption(InputConstants.KEY_RSHIFT);
        gui.modOptions.addAll(Arrays.asList(rgb.red, rgb.green, rgb.blue, rgb.rainbow, keybind));
        ModManager.addMod(ModCategory.GUI, gui);

        final ClickGUI clickGUI = new ClickGUI();

        EventBus.register(ClientTickEvent.class, event -> {
            ColorUtil.ACTIVE_FOREGROUND_COLOR = rgb.getColor();

            if (InputConstants.isKeyDown(window, keybind.keyCode) && (mc.screen == null || mc.screen instanceof TitleScreen))
                mc.setScreen(clickGUI);

            return true;
        });
    }
}
