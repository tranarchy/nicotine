package nicotine.mod.mods.general;

import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SliderOption;

import java.util.Arrays;

public class Render extends Mod {

    public static final SliderOption lineWidth = new SliderOption("LineWidth", 2, 1, 5, false);
    public static final SliderOption alpha = new SliderOption("Alpha", 50, 10, 255);

    public Render() {
        super(ModCategory.General, "Render");
        this.alwaysEnabled = true;
        this.addOptions(Arrays.asList(lineWidth, alpha));
    }
}
