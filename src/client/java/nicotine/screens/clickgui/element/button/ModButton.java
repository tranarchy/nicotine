package nicotine.screens.clickgui.element.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import nicotine.mod.Mod;
import nicotine.screens.clickgui.ClickGUI;
import nicotine.util.ColorUtil;
import nicotine.util.render.GUI;

import java.util.ArrayList;
import java.util.List;

import static nicotine.util.Common.mc;

public class ModButton extends GUIButton {
    public Mod mod;
    public static Mod selectedMod;

    public ModButton(Mod mod, int x, int y) {
        super(x, y);

        this.text = mod.name;
        this.mod = mod;
        this.height = mc.font.lineHeight + 3;
    }

    @Override
    public void click(double mouseX, double mouseY) {
        selectedMod = this.mod;
    }

    @Override
    public void draw(GuiGraphics context, double mouseX, double mouseY) {
        int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());
        this.color = this.mod.enabled || this.mod.alwaysEnabled ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR;

        if (this.mod == selectedMod) {
            final int PADDING = 5;
            context.fill(
                    this.x - PADDING + 1,
                    this.y - 3,
                    this.x + this.width,
                    this.y + this.height + 1,
                    ColorUtil.SELECTED_BACKGROUND_COLOR
            );

            GUI.drawBorderHorizontal(
                    context,
                    this.x - PADDING + 1,
                    this.y - 3,
                    this.width + PADDING - 2,
                    this.height + 3,
                    dynamicColor
            );
        }

        if (mouseOverButton(mouseX, mouseY)) {
            this.text = " " + this.text;

            if (ClickGUI.showDescription && !this.mod.description.isBlank()) {
                List<Component> description = new ArrayList<>();
                String[] splitDescription = this.mod.description.split("\n");
                for (int j = 0; j < splitDescription.length; j++) {
                    description.add(Component.literal(splitDescription[j]));
                    if (j + 1 < splitDescription.length) {
                        description.add(Component.literal(""));
                    }
                }

                context.setComponentTooltipForNextFrame(mc.font, description, (int) mouseX + 3, (int) mouseY + splitDescription.length * 3);
            }
        }

        context.drawString(mc.font, this.text, this.x, this.y + 1, this.color, true);
    }
}
