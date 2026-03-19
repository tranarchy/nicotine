package nicotine.screens.clickgui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.KeyEvent;
import nicotine.mod.mods.gui.GUI;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.SliderButton;
import nicotine.screens.clickgui.element.button.ToggleButton;
import nicotine.screens.clickgui.element.misc.Square;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class ColorSelectionScreen extends BaseScreen {

    private RGBOption rgbOption;

    public ColorSelectionScreen(RGBOption rgbOption) {
        super("Color selection screen", new Window(0, 0, 0, 0));

        this.rgbOption = rgbOption;
        window.width = GUI.screen.window.width / 2;
        window.height = GUI.screen.window.height / 2 - 70;
    }

    public void addDrawable() {
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
        elementPosY += 8;

        Square square = new Square(window.x + (window.width / 2) - 10, elementPosY, 20, 20, rgbOption.getColor());
        window.add(square);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.key() == InputConstants.KEY_ESCAPE) {
           mc.setScreen(GUI.screen);
        }

        return true;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        window.centerPosition();
        window.elements.clear();

        addDrawable();

        window.draw(context, mouseX, mouseY);
    }
}
