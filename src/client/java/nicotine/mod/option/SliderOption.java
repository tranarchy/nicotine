package nicotine.mod.option;

public class SliderOption extends ModOption {
    public float value;
    public float minValue;
    public float maxValue;
    public boolean decimal;

    public SliderOption(String name, float value, float minValue, float maxValue, boolean decimal) {
        super(name);

        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.decimal = decimal;
    }

    public SliderOption(String name, float value, float minValue, float maxValue) {
        super(name);

        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.decimal = false;
    }
}
