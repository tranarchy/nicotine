package nicotine.mod.mods.render;

import nicotine.events.RenderCrosshairEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;

import java.awt.*;

import static nicotine.util.Common.*;

public class Crosshair {
    public static void init() {
        Mod crosshair = new Mod();
        crosshair.name = "Crosshair";
        SliderOption width = new SliderOption(
                "Width",
                4,
                1,
                30
        );
        SliderOption red = new SliderOption(
                "Red",
                255,
                0,
                255
        );
        SliderOption green = new SliderOption(
                "Green",
                255,
                0,
                255
        );
        SliderOption blue = new SliderOption(
                "Blue",
                255,
                0,
                255
        );
        ToggleOption noCrosshair = new ToggleOption("NoCrosshair", false);
        ToggleOption rainbowColor = new ToggleOption("RainbowColor", false);
        crosshair.modOptions.add(width);
        crosshair.modOptions.add(red);
        crosshair.modOptions.add(green);
        crosshair.modOptions.add(blue);
        crosshair.modOptions.add(rainbowColor);
        crosshair.modOptions.add(noCrosshair);
        ModManager.modules.get(ModCategory.Render).add(crosshair);

        EventBus.register(RenderCrosshairEvent.class, event -> {
            if (!crosshair.enabled)
                return true;

            if (noCrosshair.enabled)
                return false;


            int colorVal = new Color(red.value / 255, green.value / 255, blue.value / 255).getRGB();

            if (rainbowColor.enabled)
                colorVal = Colors.rainbow;

            int widthVal = (int) width.value;

            final int centerWidth = mc.getWindow().getScaledWidth() / 2;
            final int centerHeight = (mc.getWindow().getScaledHeight() / 2) - 1;

            event.context.drawVerticalLine(centerWidth, centerHeight - widthVal - 1, centerHeight + widthVal + 1, colorVal);
            event.context.drawHorizontalLine(centerWidth - widthVal, centerWidth + widthVal, centerHeight, colorVal);

            return false;
        });
    }
}
