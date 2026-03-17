package nicotine.screens.clickgui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.screens.clickgui.element.Element;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.SliderButton;
import nicotine.screens.clickgui.element.button.ToggleButton;
import nicotine.screens.clickgui.element.misc.Square;
import nicotine.util.Settings;
import nicotine.util.render.GUI;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class ColorSelectionScreen extends Screen {

    private RGBOption rgbOption;

    private Window window;

    public ColorSelectionScreen(RGBOption rgbOption) {
        super(Component.literal("Color selection screen"));

        this.rgbOption = rgbOption;
        window = new Window(0, 0, 0, 0);
        window.width = ClickGUI.window.width / 2;
        window.height = ClickGUI.window.height / 2 - 70;
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
            Settings.save();
            this.onClose();
        }

        return true;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double offsetX, double offsetY) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        for (Element element : window.elements) {
            if (element instanceof SliderButton sliderButton) {
                if (GUI.mouseOver(sliderButton.sliderX, sliderButton.sliderY, sliderButton.sliderWidth, sliderButton.sliderHeight, mouseX, mouseY))  {
                    sliderButton.click(mouseX, mouseY);
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubled) {
        if (mouseButtonEvent.input() != InputConstants.MOUSE_BUTTON_LEFT)
            return true;

        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        for (Element element : window.elements) {
           if (element instanceof ToggleButton toggleButton && toggleButton.mouseOverButton(mouseX, mouseY)) {
               toggleButton.click(mouseX, mouseY);
          }
        }

        return true;
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (ClickGUI.blur) {
            this.renderBlurredBackground(context);
            this.renderMenuBackground(context);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        window.centerPosition();
        window.elements.clear();

        addDrawable();

        window.draw(context, mouseX, mouseY);
    }
}
