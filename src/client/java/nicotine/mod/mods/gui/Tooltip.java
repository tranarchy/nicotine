package nicotine.mod.mods.gui;

import nicotine.screens.clickgui.ClickGUI;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;

public class Tooltip {
    public static void init() {
        Mod tooltip = new Mod("Tooltip") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;
                ClickGUI.showDescription = this.enabled;
            }
        };
        ModManager.addMod(ModCategory.GUI, tooltip);
    }
}
