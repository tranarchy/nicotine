package nicotine.screens.clickgui.element.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import nicotine.mod.option.SliderOption;
import nicotine.util.ColorUtil;
import nicotine.util.render.GUI;

import static nicotine.util.Common.mc;

public class SliderButton extends GUIButton {
    public int sliderX;
    public int sliderY;
    public int sliderWidth;
    public int sliderHeight;
    public SliderOption sliderOption;

    public SliderButton(SliderOption sliderOption, int x, int y, int sliderX, int sliderY, int sliderWidth, int sliderHeight) {
        super(x, y);

        this.text = sliderOption.name;
        this.height = mc.font.lineHeight + 3;
        this.sliderOption = sliderOption;
        this.sliderX = sliderX;
        this.sliderY = sliderY;
        this.sliderWidth = sliderWidth;
        this.sliderHeight = sliderHeight;
    }

    @Override
    public void click(double mouseX, double mouseY) {
        float value = sliderOption.minValue + ((((float) (Math.round(mouseX) - this.sliderX) / this.sliderWidth)) * (sliderOption.maxValue - sliderOption.minValue));
        value = Math.round(value * 100.0f) / 100.0f;

        if (sliderOption.decimal) {
            sliderOption.value = value;
        } else {
            sliderOption.value =  Math.round(value);
        }
    }

    @Override
    public void draw(GuiGraphics context, double mouseX, double mouseY) {
        context.fill(
                this.sliderX,
                this.sliderY,
                this.sliderX + this.sliderWidth,
                this.sliderY + this.height - 1,
                ColorUtil.SELECTED_BACKGROUND_COLOR
        );

        GUI.drawBorder(
                context,
                this.sliderX,
                this.sliderY,
                this.sliderWidth,
                this.sliderHeight,
                ColorUtil.BORDER_COLOR
        );

        float normalizedSliderValue = (sliderOption.value - sliderOption.minValue) / (sliderOption.maxValue - sliderOption.minValue);
        int sliderPosX = (int) (this.sliderX + (this.sliderWidth * normalizedSliderValue));

        if (sliderOption.value == sliderOption.maxValue) {
            sliderPosX -= 3;
        }

        context.fill(sliderPosX, this.sliderY, sliderPosX + 3, this.sliderY + this.height - 1, ColorUtil.ACTIVE_FOREGROUND_COLOR);
        GUI.drawBorder(context, sliderPosX, this.sliderY, 3, this.height - 2, ColorUtil.BORDER_COLOR);

        String sliderText = sliderOption.decimal ? Float.toString(sliderOption.value) : Integer.toString((int)sliderOption.value);

        context.drawString(
                mc.font,
                Component.literal(sliderText),
                (this.sliderX + (this.sliderWidth  / 2)) - (mc.font.width(sliderText) / 2),
                this.y,
                ColorUtil.FOREGROUND_COLOR,
                true
        );

        context.drawString(mc.font, this.text, this.x, this.y, color, true);
    }
}
