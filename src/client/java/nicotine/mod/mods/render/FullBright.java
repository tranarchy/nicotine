package nicotine.mod.mods.render;

import net.minecraft.client.option.SimpleOption;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;

import static nicotine.util.Common.mc;

public class FullBright {
    public static void init() {
        SimpleOption<Double> gammaOption = mc.options.getGamma();

        Mod fullBright = new Mod("FullBright") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;
                gammaOption.setValue(enabled ? 1000000.0 : 1.0);
            }
        };
        ModManager.addMod(ModCategory.Render, fullBright);
    }
}
