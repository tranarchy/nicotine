package nicotine.screens.clickgui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.input.KeyEvent;
import nicotine.mod.mods.gui.GUI;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.SliderButton;
import nicotine.screens.clickgui.element.button.ToggleButton;
import nicotine.screens.clickgui.element.misc.Square;
import nicotine.screens.clickgui.element.misc.Text;

import java.awt.*;
import java.util.Arrays;
import java.util.HexFormat;

import static nicotine.util.Common.*;

public class ColorSelectionScreen extends BaseScreen {

    private RGBOption rgbOption;

    public ColorSelectionScreen(RGBOption rgbOption) {
        super("Color selection screen", new Window(0, 0, 0, 0));

        this.rgbOption = rgbOption;
        window.width = 160;
        window.height = 105;
    }

    @Override
    protected void addDrawables() {
        int elementPosX = window.x + 5;
        int elementPosY = window.y + 10;

        for (SliderOption sliderOption : Arrays.asList(rgbOption.red, rgbOption.green, rgbOption.blue)) {

            int buttonWidth = mc.font.width(sliderOption.name);

            SliderButton sliderButton = new SliderButton(
                    sliderOption,
                    elementPosX,
                    elementPosY,
                    elementPosX + buttonWidth + 3,
                    elementPosY - 2,
                    window.width - buttonWidth - 15,
                    mc.font.lineHeight + 1
            );

            window.add(sliderButton);

            elementPosY += 16;
        }

        ToggleButton rainbowButton = new ToggleButton(rgbOption.rainbow, elementPosX, elementPosY);
        window.add(rainbowButton);
        elementPosY += 10;

        String hexString = '#' + Integer.toHexString(rgbOption.getColor()).substring(2);
        Text hexText = new Text(hexString, window.x + (window.width / 2) - (mc.font.width(hexString) / 2), elementPosY);
        window.add(hexText);
        elementPosY += 12;

        Square square = new Square(window.x + (window.width / 2) - 10, elementPosY, 20, 20, rgbOption.getColor());
        window.add(square);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.key() == InputConstants.KEY_ESCAPE) {
           mc.setScreen(GUI.screen);
        } else if (keyEvent.hasControlDown()) {
            if (keyEvent.key() == InputConstants.KEY_V) {

                String hexColor = mc.keyboardHandler.getClipboard().trim();

                if (hexColor.isEmpty()) {
                    return true;
                }

                if (hexColor.charAt(0) != '#') {
                    hexColor = '#' + hexColor;
                }

                if (hexColor.length() < 7) {
                    return true;
                }

                if (hexColor.substring(1).codePoints().allMatch(HexFormat::isHexDigit)) {
                    Color color = Color.decode(hexColor.substring(0, 7));

                    rgbOption.red.value = color.getRed();
                    rgbOption.green.value = color.getGreen();
                    rgbOption.blue.value = color.getBlue();
                }
            } else if (keyEvent.key() == InputConstants.KEY_C) {
                mc.keyboardHandler.setClipboard(Integer.toHexString(rgbOption.getColor()).substring(2));
            }
        }

        return true;
    }
}
