package nicotine.clickgui.guibutton;

import nicotine.mod.Mod;
import nicotine.mod.option.ModOption;

public class SliderButton extends OptionButton {
    public int sliderX;
    public int sliderY;
    public int sliderWidth;
    public int sliderHeight;

    public SliderButton(int x, int y, int width, int height, int sliderX, int sliderY, int sliderWidth, int sliderHeight, Mod mod, ModOption modOption) {
        super(x, y, width, height, mod, modOption);

        this.sliderX = sliderX;
        this.sliderY = sliderY;
        this.sliderWidth = sliderWidth;
        this.sliderHeight = sliderHeight;
    }
}
