package nicotine.mod.mods.render;

import net.minecraft.client.option.SimpleOption;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

import static nicotine.util.Common.*;

public class FullBright {
    public static void init() {
        Mod fullBright = new Mod();
        fullBright.name = "FullBright";
        ModManager.modules.get(ModCategory.Render).add(fullBright);

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
