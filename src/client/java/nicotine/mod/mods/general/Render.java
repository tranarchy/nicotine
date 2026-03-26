package nicotine.mod.mods.general;

import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SliderOption;

public class Render extends Mod {

    public static final SliderOption lineWidth = new SliderOption("LineWidth", 2, 1, 5, false);

    public Render() {
        super(ModCategory.General, "Render");
        this.alwaysEnabled = true;
        this.addOptions(lineWidth);
    }
}
