package nicotine.mods.misc;

import net.minecraft.client.option.SimpleOption;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;
import static nicotine.util.Common.*;

public class SmartFocus {
    private static int originalFps = -1;

    public static void init() {
        Mod smartFocus = new Mod();
        smartFocus.name = "SmartFocus";
        modules.get(Category.Misc).add(smartFocus);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!smartFocus.enabled)
                return true;

            SimpleOption<Integer> maxFps = mc.options.getMaxFps();

            if (mc.isWindowFocused()) {
                if (originalFps != -1)
                    maxFps.setValue(originalFps);
                originalFps = maxFps.getValue();
            } else {
               maxFps.setValue(10);
            }

            return true;

        });
    }
}
