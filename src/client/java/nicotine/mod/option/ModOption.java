package nicotine.mod.option;

public class ModOption {
    public String name;
    public String id;

    public ModOption(String name) {
        this.name = name;
        this.id = name;
    }

    public ModOption(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
