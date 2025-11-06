package nicotine.mod.mods.gui;

import nicotine.screens.clickgui.ClickGUI;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;

public class Blur {
    public static void init() {
        Mod blur = new Mod("Blur") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;
                ClickGUI.blur = this.enabled;
            }
        };
        ModManager.addMod(ModCategory.GUI, blur);
    }
}
