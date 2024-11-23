package nicotine.clickgui.custombutton;

import nicotine.mod.Mod;

public class ModButton extends CustomButton {
    public Mod mod;

    public ModButton(int x, int y, int width, int height, Mod mod) {
        super(x, y, width, height);

        this.mod = mod;
    }
}
