package nicotine.mod.option;

public class ToggleOption extends ModOption {
    public boolean enabled;

    public ToggleOption(String name) {
        super(name);

        this.enabled = false;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }
}
