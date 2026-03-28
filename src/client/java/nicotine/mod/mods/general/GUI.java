package nicotine.mod.mods.general;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.TitleScreen;
import nicotine.mod.option.SliderOption;
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

    public static final ToggleOption tooltip = new ToggleOption("Tooltip");
    public static final ToggleOption blur = new ToggleOption("Blur");
    public static final ToggleOption pulsatingColor = new ToggleOption("PulsatingColor", true);
    private final SliderOption bgAlpha = new SliderOption("BGAlpha", 255, 0, 255);
    private final RGBOption rgb = new RGBOption("RGB");

    public GUI() {
        super(ModCategory.General, "GUI");
        this.alwaysEnabled = true;
        this.keybind.keyCode = InputConstants.KEY_RSHIFT;
        this.addOptions(Arrays.asList(tooltip, blur, pulsatingColor, bgAlpha, rgb));
    }

    @Override
    protected void init() {
        screen = new ClickGUI();

        EventBus.register(ClientTickEvent.class, event -> {
            ColorUtil.ACTIVE_FOREGROUND_COLOR = rgb.getColor();
            ColorUtil.BACKGROUND_COLOR = ColorUtil.changeAlpha(ColorUtil.BACKGROUND_COLOR, (int)bgAlpha.value);

            if (InputConstants.isKeyDown(window, keybind.keyCode) && (mc.screen == null || mc.screen instanceof TitleScreen))
                mc.setScreen(screen);

            return true;
        });
    }
}
