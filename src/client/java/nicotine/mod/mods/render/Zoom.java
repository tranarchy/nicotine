package nicotine.mod.mods.render;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.ChatScreen;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.window;

public class Zoom extends Mod  {
    private int defaultFov = 0;

    private final SliderOption zoomFov = new SliderOption(
            "FOV",
            10,
            5,
            30
    );

    public Zoom() {
        super(ModCategory.Render, "Zoom");

        this.keybind.keyCode = InputConstants.KEY_C;
        this.addOptions(zoomFov);
    }

    @Override
    protected void init() {
        OptionInstance<Integer> fovOption = mc.options.fov();

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled || !InputConstants.isKeyDown(window, keybind.keyCode) ||
                    mc.screen instanceof ChatScreen || mc.screen instanceof BaseScreen) {
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
