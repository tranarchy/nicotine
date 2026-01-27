package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.KeyEvent;
import nicotine.mod.option.KeybindOption;

import static nicotine.util.Common.*;

public class KeybindButton extends GUIButton {
    private final KeybindOption keybindOption;
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
            return "";
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

    public KeybindButton(KeybindOption keybindOption, int x, int y, boolean clicked) {
        super(x, y);
        this.keybindOption = keybindOption;
        this.text = String.format("%s [%s]", keybindOption.name, formatKeybind(keyCodeToString(keybindOption.keyCode)));
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
    }

    @Override
    public void click(double mouseX, double mouseY) {
        selectedKeybindOption = this.keybindOption;
    }

    @Override
    public void draw(GuiGraphics context, double mouseX, double mouseY) {
        if (selectedKeybindOption == this.keybindOption) {
            this.text = String.format("%s [_]", keybindOption.name);
            this.width = mc.font.width(this.text);
        } else {
            this.text = String.format("%s [%s]", keybindOption.name, formatKeybind(keyCodeToString(keybindOption.keyCode)));
        }

        context.drawString(mc.font, this.text, this.x, this.y, color, true);
    }
}
