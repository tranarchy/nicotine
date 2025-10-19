package nicotine.mod.option;

public class ModOption {
    public String name;
    public String id;
    public boolean subOption;

    public ModOption(String name) {
        this.name = name;
        this.id = name;
        this.subOption = false;
    }

    public ModOption(String name, String id) {
        this.name = name;
        this.id = id;
        this.subOption = false;
    }
}
