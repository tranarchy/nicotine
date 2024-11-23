package nicotine.mod.mods.gui;

import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;

import java.awt.*;

public class GUIColors {
    public static void init() {
        Mod color = new Mod();
        color.name = "Color";
        color.alwaysEnabled = true;

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
        color.modOptions.add(red);
        color.modOptions.add(green);
        color.modOptions.add(blue);
        ModManager.modules.get(ModCategory.GUI).add(color);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            int colorVal = new Color(red.value / 255, green.value / 255, blue.value / 255).getRGB();

            Colors.ACTIVE_FOREGROUND_COLOR = colorVal;
            Colors.CATEGORY_BACKGROUND_COLOR = colorVal;

            return true;
        });
    }
}
