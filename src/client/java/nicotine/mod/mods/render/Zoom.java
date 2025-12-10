package nicotine.mod.mods.render;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.ChatScreen;
import nicotine.screens.clickgui.ClickGUI;
import nicotine.events.ClientLevelTickEvent;
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
        KeybindOption keybind = new KeybindOption(InputConstants.KEY_C);
        zoom.modOptions.addAll(Arrays.asList(zoomFov, keybind));
        ModManager.addMod(ModCategory.Render, zoom);

        OptionInstance<Integer> fovOption = mc.options.fov();

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!zoom.enabled || !InputConstants.isKeyDown(window, keybind.keyCode) ||
                    mc.screen instanceof ChatScreen || mc.screen instanceof ClickGUI) {
                int fov =  fovOption.get();
                if (fov == zoomFov.value)
                    fovOption.set(defaultFov);
                else
                    defaultFov = fov;
                return true;
            }

            fovOption.set((int)zoomFov.value);

            return true;
        });


    }
}
