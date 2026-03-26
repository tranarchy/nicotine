package nicotine.mod.mods.general;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.TitleScreen;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.screens.clickgui.ClickGUI;
import nicotine.events.ClientTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.RGBOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class GUI extends Mod {

    public static BaseScreen screen;

    private final RGBOption rgb = new RGBOption("RGB");
    public static final ToggleOption tooltip = new ToggleOption("Tooltip");
    public static final ToggleOption blur = new ToggleOption("Blur");

    public GUI() {
        super(ModCategory.General, "GUI");
        this.alwaysEnabled = true;
        this.keybind.keyCode = InputConstants.KEY_RSHIFT;
        this.addOptions(Arrays.asList(tooltip, blur, rgb));
    }

    @Override
    protected void init() {
        screen = new ClickGUI();

        EventBus.register(ClientTickEvent.class, event -> {
            ColorUtil.ACTIVE_FOREGROUND_COLOR = rgb.getColor();

            if (InputConstants.isKeyDown(window, keybind.keyCode) && (mc.screen == null || mc.screen instanceof TitleScreen))
                mc.setScreen(screen);

            return true;
        });
    }
}
