package nicotine.screens.clickgui.element.button;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import nicotine.mod.option.DropDownOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static nicotine.util.Common.*;

public class DropDownButton extends GUIButton {
    public int dropDownX;
    public int dropDownY;
    public int dropDownWidth;
    public int dropDownHeight;

    private final DropDownOption dropDownOption;

    public static DropDownOption selectedDropDownOption;
    public static int dropDownStartX;
    public static int dropDownStartY;

    public DropDownButton(DropDownOption dropDownOption, int x, int y) {
        super(dropDownOption.name, x, y);
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;
        this.dropDownOption = dropDownOption;

        this.dropDownX = this.x + this.width + 3;
        this.dropDownY = this.y;
        this.dropDownWidth = mc.font.width(this.dropDownOption.value + " ᎒");
        this.dropDownHeight = mc.font.lineHeight;
    }

    @Override
    public void click(double mouseX, double mouseY, int input) {
        if (input != InputConstants.MOUSE_BUTTON_LEFT)
            return;

        if (selectedDropDownOption == this.dropDownOption) {
            selectedDropDownOption = null;
        } else {
            selectedDropDownOption = this.dropDownOption;
            dropDownStartX = dropDownX;
            dropDownStartY = dropDownY + dropDownHeight + 2;
        }
    }

    @Override
    public boolean mouseOverButton(double mouseX, double mouseY) {
        return Render2D.mouseOver(this.dropDownX, this.dropDownY, this.dropDownWidth, this.dropDownHeight, mouseX, mouseY);
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        super.draw(context, mouseX, mouseY);

        Render2D.drawBorderAroundText(context, dropDownOption.value + " ᎒", dropDownX, dropDownY, 1, ColorUtil.BORDER_COLOR);
        context.text(mc.font, dropDownOption.value + " ᎒", dropDownX, dropDownY, ColorUtil.FOREGROUND_COLOR);
    }

    public static List<ToggleButton> getDropDownButtons() {
        List<ToggleButton> dropDownElements = new ArrayList<>();

        int posX = dropDownStartX;
        int posY = dropDownStartY;

       List<String> modeList = Arrays.stream(selectedDropDownOption.modes).sorted(Comparator.comparing(mc.font::width)).toList();

        for (int i = 0; i < selectedDropDownOption.modes.length; i++) {
            String mode = selectedDropDownOption.modes[i];

            ToggleButton dropDownElement =  new ToggleButton(
                    new ToggleOption(mode) {
                        @Override
                        public void toggle() {
                            selectedDropDownOption.value = mode;
                            selectedDropDownOption = null;
                        }
                    },
                    posX,
                    posY
            ) {
                @Override
                public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
                    this.width = mc.font.width(modeList.getLast());
                    Render2D.drawBorderAroundText(context, this.x, this.y, this.width + 2, 1, ColorUtil.BORDER_COLOR);
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
