package nicotine.mod;

import nicotine.mod.option.ModOption;

import java.util.ArrayList;
import java.util.List;

public class Mod {
    public String name;
    public String description = "";

    public boolean enabled = false;
    public boolean alwaysEnabled = false;

    public List<ModOption> modOptions = new ArrayList<>();
    public boolean optionsVisible = false;


    public Mod(String name) {
        this.name = name;
    }

    public Mod(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }
}
