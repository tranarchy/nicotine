package nicotine.mod;

import nicotine.mod.option.ModOption;

import java.util.ArrayList;
import java.util.List;

public class Mod {
    public String name;
    public boolean enabled = false;
    public boolean alwaysEnabled = false;
    public List<ModOption> modOptions = new ArrayList<>();
    public boolean optionsVisible = false;
}
