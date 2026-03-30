package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import nicotine.mod.option.KeybindOption;
import nicotine.screens.clickgui.element.misc.Text;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import static nicotine.util.Common.*;

public class KeybindButton extends GUIButton {
    private final KeybindOption keybindOption;
    private final Text boxText;
    public static KeybindOption selectedKeybindOption = null;

    private String formatKeybind(String keybind) {
        String keyBindText = "";
        String[] splitKeybind = keybind.split(" ");
        if (splitKeybind.length >= 2) {
            for (String kbText : splitKeybind) {
                keyBindText += kbText.substring(0, 1);
            }
        } else {
            keyBindText = keybind;
        }

        return keyBindText;
    }

    private String keyCodeToString(int keyCode) {
        if (keyCode == -1)
            return "Unset";
        else if (keyCode < 8) {
            return switch (keyCode) {
                case InputConstants.MOUSE_BUTTON_LEFT -> "MBL";
                case InputConstants.MOUSE_BUTTON_RIGHT -> "MBR";
                case InputConstants.MOUSE_BUTTON_MIDDLE -> "MBM";
                default -> String.format("MB%d", keyCode);
            };
        } else {
            return InputConstants.getKey(new KeyEvent(keyCode, 0, 0)).getDisplayName().getString();
        }
    }

    public KeybindButton(KeybindOption keybindOption, int x, int y) {
        super(x, y);
        this.keybindOption = keybindOption;
        this.text = keybindOption.name;
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;

        this.boxText = new Text(formatKeybind(keyCodeToString(keybindOption.keyCode)), this.x + this.width + 4, this.y);
    }

    @Override
    public boolean mouseOverElement(double mouseX, double mouseY) {
        return Render2D.mouseOver(this.x, this.y, this.width + this.boxText.width + 4, this.height, mouseX, mouseY);
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input == InputConstants.MOUSE_BUTTON_LEFT) {
            selectedKeybindOption = this.keybindOption;
        } else if (input == InputConstants.MOUSE_BUTTON_RIGHT) {
            keybindOption.keyCode = -1;
        }
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        if (selectedKeybindOption == this.keybindOption) {
            this.boxText.text = "_";
            //this.width = mc.font.width(this.text);
        } else {
            this.boxText.text = formatKeybind(keyCodeToString(keybindOption.keyCode));
        }

        Render2D.drawBorderAroundText(context, this.boxText.text, this.boxText.x, this.boxText.y, 1, ColorUtil.BORDER_COLOR);
        this.boxText.draw(context, mouseX, mouseY);

        super.draw(context, mouseX, mouseY);

        if (mouseOverElement(mouseX, mouseY)) {
            drawUnderline(context);
        }
    }
}
