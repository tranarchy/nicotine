package nicotine.mod.mods.render;

import net.minecraft.client.OptionInstance;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;

import static nicotine.util.Common.mc;

public class FullBright {
    public static void init() {
        OptionInstance<Double> gammaOption = mc.options.gamma();

        Mod fullBright = new Mod("FullBright") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;
                gammaOption.set(enabled ? 1000000.0 : 1.0);
            }
        };
        ModManager.addMod(ModCategory.Render, fullBright);
    }
}
