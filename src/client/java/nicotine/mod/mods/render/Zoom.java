package nicotine.mod.mods.render;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import nicotine.screens.clickgui.ClickGUI;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.window;

public class Zoom {
    private static int defaultFov = 0;

    public static void init() {
        Mod zoom = new Mod("Zoom");
        SliderOption zoomFov = new SliderOption(
                "FOV",
                10,
                5,
                30
        );
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_C);
        zoom.modOptions.addAll(Arrays.asList(zoomFov, keybind));
        ModManager.addMod(ModCategory.Render, zoom);

        SimpleOption<Integer> fovOption = mc.options.getFov();

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!zoom.enabled || !InputUtil.isKeyPressed(window, keybind.keyCode) ||
                    mc.currentScreen instanceof ChatScreen || mc.currentScreen instanceof ClickGUI) {
                int fov =  fovOption.getValue();
                if (fov == zoomFov.value)
                    fovOption.setValue(defaultFov);
                else
                    defaultFov = fov;
                return true;
            }

            fovOption.setValue((int)zoomFov.value);

            return true;
        });


    }
}
