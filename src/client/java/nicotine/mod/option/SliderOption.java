package nicotine.mod.option;

public class SliderOption extends ModOption {
    public float value;
    public float minValue;
    public float maxValue;

    public SliderOption(String name, float value, float minValue, float maxValue) {
        super(name);

        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
}
