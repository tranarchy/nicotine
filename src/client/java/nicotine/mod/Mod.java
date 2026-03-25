package nicotine.mod;

import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ModOption;

import java.util.ArrayList;
import java.util.List;

public class Mod {
    public ModCategory modCategory;
    public String name;
    public String description = "";

    public boolean enabled = false;
    public boolean alwaysEnabled = false;

    public KeybindOption keybind = new KeybindOption(-1);

    public List<ModOption> modOptions = new ArrayList<>();

    public Mod(ModCategory modCategory, String name, String description) {
        this.modCategory = modCategory;
        this.name = name;
        this.description = description;

        this.init();
        this.modOptions.add(keybind);
    }

    public Mod(ModCategory modCategory, String name) {
        this.modCategory = modCategory;
        this.name = name;

        this.init();
        this.modOptions.add(keybind);
    }

    protected void init() {}

    public void toggle() {
        this.enabled = !this.enabled;
    }

    public void addOptions(List<ModOption> options) {
        this.modOptions.addAll(0, options);
    }

    public void addOptions(ModOption option) {
        this.modOptions.addFirst(option);
    }
}
