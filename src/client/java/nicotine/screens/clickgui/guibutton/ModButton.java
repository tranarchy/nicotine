package nicotine.screens.clickgui.guibutton;

import nicotine.mod.Mod;

public class ModButton extends GUIButton {
    public Mod mod;

    public ModButton(int x, int y, int width, int height, Mod mod) {
        super(x, y, width, height);

        this.mod = mod;
    }

    public ModButton(ModButton modButton) {
        super(modButton.x, modButton.y, modButton.width, modButton.height);

        this.mod = modButton.mod;
    }
}
