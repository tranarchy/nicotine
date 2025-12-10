package nicotine.mod.mods.render;

import net.minecraft.client.CameraType;
import nicotine.events.RenderCrosshairEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Crosshair {
    public static void init() {
        Mod crosshair = new Mod("Crosshair");
        SliderOption width = new SliderOption(
                "Width",
                4,
                1,
                30
        );
        RGBOption rgb = new RGBOption();
        ToggleOption noCrosshair = new ToggleOption("NoCrosshair");
        crosshair.modOptions.addAll(Arrays.asList(width, rgb.red, rgb.green, rgb.blue, rgb.rainbow, noCrosshair));
        ModManager.addMod(ModCategory.Render, crosshair);

        EventBus.register(RenderCrosshairEvent.class, event -> {
            if (!crosshair.enabled)
                return true;

            if (noCrosshair.enabled || mc.options.getCameraType() != CameraType.FIRST_PERSON)
                return false;

            int widthVal = (int) width.value;

            final int centerWidth = mc.getWindow().getGuiScaledWidth() / 2;
            final int centerHeight = (mc.getWindow().getGuiScaledHeight() / 2) - 1;

            event.context.vLine(centerWidth, centerHeight - widthVal - 1, centerHeight + widthVal + 1, rgb.getColor());
            event.context.hLine(centerWidth - widthVal, centerWidth + widthVal, centerHeight, rgb.getColor());

            return false;
        });
    }
}
