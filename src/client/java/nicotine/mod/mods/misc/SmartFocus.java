package nicotine.mod.mods.misc;

import net.minecraft.client.OptionInstance;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class SmartFocus {

    public static void init() {
        Mod smartFocus = new Mod("SmartFocus", "When the game is not in focus limits your FPS");
        SliderOption fps = new SliderOption(
                "FPS",
                5,
                1,
                30
        );
        smartFocus.modOptions.add(fps);
        ModManager.addMod(ModCategory.Misc, smartFocus);

        OptionInstance<Integer> maxFps = mc.options.framerateLimit();

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!smartFocus.enabled)
                return true;

            if (mc.isWindowActive()) {
                mc.getFramerateLimitTracker().setFramerateLimit(maxFps.get());
            } else {
                mc.getFramerateLimitTracker().setFramerateLimit((int)fps.value);
            }

            return true;

        });
    }
}
