package nicotine.mod.mods.misc;

import net.minecraft.client.option.SimpleOption;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

import static nicotine.util.Common.*;

public class SmartFocus {

    public static void init() {
        Mod smartFocus = new Mod();
        smartFocus.name = "SmartFocus";
        SliderOption fps = new SliderOption(
                "FPS",
                5,
                1,
                30
        );
        smartFocus.modOptions.add(fps);
        ModManager.modules.get(ModCategory.Misc).add(smartFocus);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!smartFocus.enabled)
                return true;

            if (mc.isWindowFocused()) {
                SimpleOption<Integer> maxFps = mc.options.getMaxFps();
                mc.getInactivityFpsLimiter().setMaxFps(maxFps.getValue());
            } else {
                mc.getInactivityFpsLimiter().setMaxFps((int)fps.value);
            }

            return true;

        });
    }
}
