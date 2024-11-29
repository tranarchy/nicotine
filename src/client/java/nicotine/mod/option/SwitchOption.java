package nicotine.mod.option;

public class SwitchOption extends ModOption {
    public String[] modes;
    public String value;

    public SwitchOption(String name, String[] modes) {
        super(name);

        this.modes = modes;
        this.value = modes[0];
    }
}
