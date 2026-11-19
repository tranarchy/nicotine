package nicotine.mod.option;

import nicotine.util.ColorUtil;

import java.awt.*;
import java.util.HexFormat;

public class RGBOption extends ModOption  {
    public SliderOption alpha;
    public SliderOption red;
    public SliderOption green;
    public SliderOption blue;
    public ToggleOption rainbow;

    public RGBOption(String name) {
        super(name);

        this.alpha = new SliderOption(
                "Alpha",
                255,
                0,
                255
        );

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
        return rainbow.enabled ? ColorUtil.changeAlpha(ColorUtil.getRainbowColor(), (int)alpha.value) : new Color(red.value / 255, green.value / 255, blue.value / 255, alpha.value / 255).getRGB();
    }

    public String getColorString() {
        return '#' + Integer.toHexString(this.getColor());
    }

    public boolean setColor(String hexColor) {
        if (hexColor.isEmpty()) {
            return false;
        }

        if (hexColor.charAt(0) != '#') {
            hexColor = '#' + hexColor;
        }

        if (hexColor.length() < 9) {
            return false;
        }

        if (hexColor.substring(1).codePoints().allMatch(HexFormat::isHexDigit)) {
            Long color = Long.decode(hexColor);

            return setColor(color);
        }

        return true;
    }

    public boolean setColor(long hexColor) {
        alpha.value = ((hexColor >> 24) & 0xFF);
        red.value = ((hexColor >> 16) & 0xFF);
        green.value = ((hexColor >> 8) & 0xFF);
        blue.value = (hexColor & 0xFF);

        return true;
    }

}
