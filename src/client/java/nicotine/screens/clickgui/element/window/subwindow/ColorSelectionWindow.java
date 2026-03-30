package nicotine.screens.clickgui.element.window.subwindow;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.input.KeyEvent;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.screens.clickgui.element.button.SliderButton;
import nicotine.screens.clickgui.element.button.ToggleButton;
import nicotine.screens.clickgui.element.misc.Square;
import nicotine.screens.clickgui.element.misc.Text;

import java.awt.*;
import java.util.Arrays;
import java.util.HexFormat;

import static nicotine.util.Common.*;

public class ColorSelectionWindow extends SubWindow {

    private final RGBOption rgbOption;

    public ColorSelectionWindow(BaseScreen screen, String title, RGBOption rgbOption) {
        super(screen, title, 0, 0, 160, 105);
        this.rgbOption = rgbOption;
    }

    @Override
    public void addDrawables() {
        super.addDrawables();

        int elementPosX = this.x + 5;
        int elementPosY = this.y + 10;

        for (SliderOption sliderOption : Arrays.asList(rgbOption.red, rgbOption.green, rgbOption.blue)) {

            int buttonWidth = mc.font.width(sliderOption.name);

            SliderButton sliderButton = new SliderButton(
                    sliderOption,
                    elementPosX,
                    elementPosY,
                    elementPosX + buttonWidth + 3,
                    elementPosY - 2,
                    this.width - buttonWidth - 15,
                    mc.font.lineHeight + 1
            );

            this.add(sliderButton);

            elementPosY += 16;
        }

        ToggleButton rainbowButton = new ToggleButton(rgbOption.rainbow, elementPosX, elementPosY);
        this.add(rainbowButton);
        elementPosY += 10;

        String hexString = '#' + Integer.toHexString(rgbOption.getColor()).substring(2);
        Text hexText = new Text(hexString, this.x + (this.width / 2) - (mc.font.width(hexString) / 2), elementPosY);
        this.add(hexText);
        elementPosY += 12;

        Square square = new Square(this.x + (this.width / 2) - 10, elementPosY, 20, 20, rgbOption.getColor());
        this.add(square);
    }

    @Override
    public boolean handleKeyPress(KeyEvent keyEvent) {
       if (keyEvent.hasControlDown() && this.mouseOverElement(this.screen.mouseX, this.screen.mouseY)) {
            if (keyEvent.key() == InputConstants.KEY_V) {

                String hexColor = mc.keyboardHandler.getClipboard().trim();

                if (hexColor.isEmpty()) {
                    return false;
                }

                if (hexColor.charAt(0) != '#') {
                    hexColor = '#' + hexColor;
                }

                if (hexColor.length() < 7) {
                    return false;
                }

                if (hexColor.substring(1).codePoints().allMatch(HexFormat::isHexDigit)) {
                    Color color = Color.decode(hexColor.substring(0, 7));

                    rgbOption.red.value = color.getRed();
                    rgbOption.green.value = color.getGreen();
                    rgbOption.blue.value = color.getBlue();

                    return false;
                }
            } else if (keyEvent.key() == InputConstants.KEY_C) {
                mc.keyboardHandler.setClipboard(Integer.toHexString(rgbOption.getColor()).substring(2));

                return true;
            }
        }

        return false;
    }
}
