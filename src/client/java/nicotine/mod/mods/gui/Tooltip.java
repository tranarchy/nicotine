package nicotine.mod.mods.gui;

import nicotine.screens.clickgui.ClickGUI;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;

public class Tooltip extends Mod {

    public Tooltip() {
        super(ModCategory.GUI, "Tooltip");
    }

    @Override
    public void toggle() {
        this.enabled = !this.enabled;
        ClickGUI.showDescription = this.enabled;
    }
}
