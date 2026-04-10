package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.mod.option.DropDownOption;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.clickgui.element.misc.Text;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static nicotine.util.Common.*;

public class DropDownButton extends GUIButton {

    private final DropDownOption dropDownOption;
    private final Text boxText;

    public static DropDownOption selectedDropDownOption;
    public static int dropDownStartX;
    public static int dropDownStartY;

    public DropDownButton(DropDownOption dropDownOption, int x, int y) {
        super(dropDownOption.name, x, y);
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
        this.dropDownOption = dropDownOption;

        this.boxText = new Text(String.format("%s%s", this.dropDownOption.value, " ᎒"), this.x + this.width + 4, this.y);
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        if (selectedDropDownOption == this.dropDownOption) {
            selectedDropDownOption = null;
        } else {
            selectedDropDownOption = this.dropDownOption;
            dropDownStartX = boxText.x;
            dropDownStartY = y + height + 2;
        }
    }

    @Override
    public boolean mouseOverElement(double mouseX, double mouseY) {
        return Render2D.mouseOver(this.boxText.x, this.boxText.y, this.boxText.width, this.height, mouseX, mouseY);
    }

    public int getFullWidth() {
        return this.width + this.boxText.width;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        context.text(mc.font, this.text, this.x, this.y, this.color, true);

        Render2D.drawBorderAroundText(context, boxText.text, this.boxText.x, this.boxText.y, 1, ColorUtil.BORDER_COLOR);
        boxText.draw(context, mouseX, mouseY);
    }

    public static List<GUIButton> getDropDownButtons() {
        List<GUIButton> dropDownElements = new ArrayList<>();

        int posX = dropDownStartX;
        int posY = dropDownStartY;

       List<String> modeList = Arrays.stream(selectedDropDownOption.modes).sorted(Comparator.comparing(mc.font::width)).toList();

        for (int i = 0; i < selectedDropDownOption.modes.length; i++) {
            String mode = selectedDropDownOption.modes[i];

            GUIButton dropDownElement =  new GUIButton(
                    mode,
                    posX,
                    posY
            ) {
                @Override
                public void click(double mouseX, double mouseY, int input) {
                    selectedDropDownOption.value = mode;
                    selectedDropDownOption.select();
                    selectedDropDownOption = null;
                }

                @Override
                public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
                    if (mouseOverElement(mouseX, mouseY)) {
                        this.color = ColorUtil.ACTIVE_FOREGROUND_COLOR;
                    }

                    this.width = mc.font.width(modeList.getLast());
                    Render2D.drawBorderAroundText(context, this.x, this.y, this.width, this.height, 1, ColorUtil.BORDER_COLOR);
                    context.text(mc.font, this.text, this.x, this.y, this.color, true);
                }
            };

            dropDownElement.z = 1;

            dropDownElements.add(dropDownElement);

            posY += mc.font.lineHeight + 2;
        }

        return dropDownElements;
    }
}
