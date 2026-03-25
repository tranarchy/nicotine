package nicotine.mod.mods.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.TitleScreen;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.screens.clickgui.ClickGUI;
import nicotine.events.ClientTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.RGBOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;

public class GUI extends Mod {

    private final RGBOption rgb = new RGBOption("RGB");
    public static BaseScreen screen;

    public GUI() {
        super(ModCategory.GUI, "GUI");
        this.alwaysEnabled = true;
        this.keybind.keyCode = InputConstants.KEY_RSHIFT;
        this.addOptions(rgb);
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
