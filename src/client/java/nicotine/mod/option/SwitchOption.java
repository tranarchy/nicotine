package nicotine.mod.option;

public class SwitchOption extends ModOption {
    public String[] modes;
    public int value;

    public SwitchOption(String name, String[] modes, int value) {
        super(name);

        this.modes = modes;
        this.value = value;
    }
}
