package nicotine.mod.option;

import nicotine.util.ColorUtil;

import java.awt.*;

public class RGBOption  {
    public SliderOption red;
    public SliderOption green;
    public SliderOption blue;
    public ToggleOption rainbow;

    public RGBOption() {
        this.red = new SliderOption(
                "Red",
                152,
                0,
                255
        );
        this.green = new SliderOption(
                "Green",
                137,
                0,
                255
        );
        this.blue = new SliderOption(
                "Blue",
                250,
                0,
                255
        );

        this.rainbow = new ToggleOption("Rainbow");
    }

    public int getColor() {
        return rainbow.enabled ? ColorUtil.rainbow : new Color(red.value / 255, green.value / 255, blue.value / 255).getRGB();
    }

}
