package nicotine.clickgui.custombutton;

import nicotine.mod.Mod;
import nicotine.mod.option.ModOption;

public class OptionButton extends CustomButton {
    public Mod mod;
    public ModOption modOption;

    public OptionButton(int x, int y, int width, int height, Mod mod, ModOption modOption) {
        super(x, y, width, height);

        this.mod = mod;
        this.modOption = modOption;
    }
}
