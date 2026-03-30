package nicotine.mod.mods.hud;

import com.mojang.blaze3d.platform.InputConstants;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.DropDownOption;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.clickgui.HUDScreen;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.window;

public class HUD extends Mod {
    public static final DropDownOption separator = new DropDownOption(
            "Separator",
            new String[]{"->", ">", "<", "=", ":", ""}
    );
    public static final ToggleOption lowercase = new ToggleOption("Lowercase");
    public static final ToggleOption bold = new ToggleOption("Bold");
    public static final ToggleOption italic = new ToggleOption("Italic");

    public static HUDScreen screen;

    public HUD() {
        super(ModCategory.HUD, "HUD");
        this.keybind.keyCode = InputConstants.KEY_H;
        this.keybind.name = "Editor";
        this.addOptions(Arrays.asList(lowercase, bold, italic, separator));
        screen = new HUDScreen();
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (InputConstants.isKeyDown(window, keybind.keyCode) && mc.screen == null)
                mc.setScreen(screen);

            return true;
        });
    }
}
