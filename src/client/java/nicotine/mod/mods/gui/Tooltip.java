package nicotine.mod.mods.gui;

import nicotine.clickgui.GUI;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;

public class Tooltip {
    public static void init() {
        Mod tooltip = new Mod("Tooltip") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;
                GUI.showDescription = this.enabled;
            }
        };
        ModManager.addMod(ModCategory.GUI, tooltip);
    }
}
