package nicotine.mod;

import nicotine.mod.option.ModOption;

import java.util.ArrayList;
import java.util.List;

public class Mod {
    public ModCategory modCategory;
    public String name;
    public String description = "";

    public boolean enabled = false;
    public boolean alwaysEnabled = false;

    public List<ModOption> modOptions = new ArrayList<>();

    public Mod(ModCategory modCategory, String name, String description) {
        this.modCategory = modCategory;
        this.name = name;
        this.description = description;

        this.init();
    }

    public Mod(ModCategory modCategory, String name) {
        this.modCategory = modCategory;
        this.name = name;

        this.init();
    }

    protected void init() {}

    public void toggle() {
        this.enabled = !this.enabled;
    }
}
