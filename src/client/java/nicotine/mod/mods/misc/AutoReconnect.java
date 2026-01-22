package nicotine.mod.mods.misc;

import nicotine.events.DisconnectEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SliderOption;
import nicotine.screens.AutoReconnectScreen;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoReconnect extends Mod {

    private final  SliderOption delay = new SliderOption(
            "Delay",
            20,
            1,
            60
    );

    public AutoReconnect() {
        super(ModCategory.Misc, "AutoReconnect");
        this.modOptions.add(delay);
    }

    @Override
    protected void init() {
        EventBus.register(DisconnectEvent.class, event -> {
            if (!this.enabled)
                return true;

            mc.setScreen(new AutoReconnectScreen((int)delay.value * 20));
            return true;
        });

    }
}
