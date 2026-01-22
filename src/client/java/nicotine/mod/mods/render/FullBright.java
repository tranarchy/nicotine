package nicotine.mod.mods.render;

import net.minecraft.client.OptionInstance;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;

import static nicotine.util.Common.mc;

public class FullBright extends Mod {

    public FullBright() {
        super(ModCategory.Render, "FullBright");
    }

    @Override
    public void toggle() {
        OptionInstance<Double> gammaOption = mc.options.gamma();
        this.enabled = !this.enabled;
        gammaOption.set(enabled ? 1000000.0 : 1.0);
    }
}
