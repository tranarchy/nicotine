package nicotine.mod.option;

public class DropDownOption extends ModOption {
    public String[] modes;
    public String value;

    public DropDownOption(String name, String[] modes) {
        super(name);

        this.modes = modes;
        this.value = modes[0];
    }
}
