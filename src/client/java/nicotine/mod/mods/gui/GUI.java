package nicotine.mod.mods.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.TitleScreen;
import nicotine.screens.clickgui.ClickGUI;
import nicotine.events.ClientTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.RGBOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class GUI extends Mod {

    private final RGBOption rgb = new RGBOption();
    private final KeybindOption keybind = new KeybindOption(InputConstants.KEY_RSHIFT);

    public GUI() {
        super(ModCategory.GUI, "GUI");
        this.alwaysEnabled = true;
        this.modOptions.addAll(Arrays.asList(rgb.red, rgb.green, rgb.blue, rgb.rainbow, keybind));
    }

    @Override
    protected void init() {
        final ClickGUI clickGUI = new ClickGUI();

        EventBus.register(ClientTickEvent.class, event -> {
            ColorUtil.ACTIVE_FOREGROUND_COLOR = rgb.getColor();

            if (InputConstants.isKeyDown(window, keybind.keyCode) && (mc.screen == null || mc.screen instanceof TitleScreen))
                mc.setScreen(clickGUI);

            return true;
        });
    }
}
