package nicotine.mod.option;

public class ToggleOption extends ModOption {
    public boolean enabled;

    public ToggleOption(String name, boolean enabled) {
        super(name);

        this.enabled = enabled;
    }
}