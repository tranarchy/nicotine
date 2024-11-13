package nicotine.mods.render;

import net.minecraft.client.option.SimpleOption;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;
import static nicotine.util.Modules.*;

public class FullBright {
    public static void init() {
        Mod fullBright = new Mod();
        fullBright.name = "FullBright";
        modules.get(Category.Render).add(fullBright);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            SimpleOption<Double> gammaOption = mc.options.getGamma();

            if (!fullBright.enabled) {
                if (gammaOption.getValue() > 1.0)
                    gammaOption.setValue(1.0);

                return true;
            }

            gammaOption.setValue(1000000.0);

            return true;
        });
    }
}
