package nicotine.mod.mods.misc;

import net.minecraft.client.OptionInstance;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class SmartFocus extends Mod {

    private final SliderOption fps = new SliderOption(
            "FPS",
            5,
            1,
            30
    );

    public SmartFocus() {
        super(ModCategory.Misc, "SmartFocus", "When the game is not in focus limits your FPS");
        this.modOptions.add(fps);
    }

    @Override
    protected void init() {
        OptionInstance<Integer> maxFps = mc.options.framerateLimit();

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
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
