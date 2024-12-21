package nicotine.mod.option;

public class KeybindOption extends ModOption {
    public int keyCode;

    public KeybindOption(int keyCode) {
        super("Key");
        this.keyCode = keyCode;
    }

    public KeybindOption(String name, int keyCode) {
        super(name);
        this.keyCode = keyCode;
    }
}
