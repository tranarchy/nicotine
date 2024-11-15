package nicotine.gui;

import net.minecraft.util.Formatting;
import nicotine.Main;
import nicotine.util.RenderGUI;
import nicotine.util.Settings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.*;
import static nicotine.util.Modules.*;
import static nicotine.util.Colors.*;

public class GUI extends Screen {
    public GUI() {
        super(Text.literal("nicotine"));
    }

    private final List<CustomButton> buttons = new ArrayList<>();

    private void getButtons() {
        int guiY = 10;
        int guiX = 10;

        int height = mc.textRenderer.fontHeight + 10;
        int width = 85;

        for (HashMap.Entry<Category, List<Mod>> modSet : modules.entrySet()) {

            Mod category = new Mod();
            category.name = modSet.getKey().toString();
            CustomButton customButton = new CustomButton(guiX, guiY, width, height, category);
            guiY += height;

            buttons.add(customButton);

            for (Mod mod : modSet.getValue())
            {
                customButton = new CustomButton(guiX, guiY, width, height, mod);
                guiY += height;
                buttons.add(customButton);
            }

            guiX += width + 15;
            guiY = 10;

        }

    }

    private void drawWatermark(DrawContext context) {
        final String watermarkText =  String.format("nicotine %sv%s", Formatting.WHITE, Main.VERSION);
        final int watermarkPosX = mc.getWindow().getScaledWidth()  - textRenderer.getWidth(watermarkText) - 10;
        final int watermarkPosY = mc.getWindow().getScaledHeight() - textRenderer.fontHeight - 10;
        context.drawText(textRenderer, watermarkText,  watermarkPosX , watermarkPosY, rainbow, true);
    }

    private void drawButtons(DrawContext context) {
        if (buttons.isEmpty())
            getButtons();

        for (CustomButton button : buttons) {
            int textColor = SEC_FOREGROUND_COLOR;
            int backgroundColor = BACKGROUND_COLOR;

            StringBuilder text = new StringBuilder(button.mod.name);

            if (Arrays.toString(Category.values()).contains(button.mod.name)) {
                backgroundColor = FOREGROUND_COLOR;
            } else if (button.mod.enabled) {
                textColor = FOREGROUND_COLOR;
                if (button.mod.modes != null) {
                    text.append(String.format(" [%s] ", button.mod.modes.get(button.mod.mode)));
                }
            }

            context.fill(button.x, button.y, button.x + button.width, button.y + button.height, backgroundColor);
            RenderGUI.drawBorder(context, button.x, button.y, button.width, button.height, FOREGROUND_COLOR);
            context.drawText(textRenderer, text.toString(), button.x + 6, button.y + 6, textColor, true);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawWatermark(context);
        drawButtons(context);

        this.getFocused();

        super.render(context, mouseX, mouseY, delta);
    }


    private boolean clickedOn(CustomButton button, double mouseX, double mouseY) {
        return (button.x <= mouseX && mouseX <= button.x + button.width && button.y <= mouseY && mouseY <= button.y + button.height);
    }

    @Override
    public boolean mouseClicked (double mouseX, double mouseY, int clickedButton) {
        for (CustomButton button : buttons) {
            if (clickedOn(button, mouseX, mouseY))
            {
                if (button.mod.modes == null) {
                    button.mod.enabled = !button.mod.enabled;
                } else if (button.mod.mode + 1 == button.mod.modes.size()) {
                    button.mod.mode = -1;
                    button.mod.enabled = false;
                } else {
                    button.mod.mode += 1;
                    button.mod.enabled = true;
                }

                Settings.save();
                break;
            }
        }

        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
       return;
    }

}
