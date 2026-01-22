package nicotine.mod.mods.gui;

import nicotine.screens.clickgui.ClickGUI;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;

public class Blur extends Mod {

    public Blur() {
        super(ModCategory.GUI, "Blur");
    }

    @Override
    public void toggle() {
        this.enabled = !this.enabled;
        ClickGUI.blur = this.enabled;
    }
}
