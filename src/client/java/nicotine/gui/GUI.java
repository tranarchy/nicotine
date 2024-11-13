package nicotine.gui;

import net.minecraft.util.Formatting;
import nicotine.Main;
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
        buttons.clear();

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

    private boolean clickedOn(CustomButton button, double mouseX, double mouseY) {
        return (button.x <= mouseX && mouseX <= button.x + button.width && button.y <= mouseY && mouseY <= button.y + button.height);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        final String watermarkText =  String.format("nicotine %sv%s", Formatting.WHITE, Main.VERSION);
        final int watermarkPosX = mc.getWindow().getScaledWidth()  - textRenderer.getWidth(watermarkText) - 10;
        final int watermarkPosY = mc.getWindow().getScaledHeight() - textRenderer.fontHeight - 10;
        context.drawText(textRenderer, watermarkText,  watermarkPosX , watermarkPosY, rainbow, true);

        getButtons();
        for (CustomButton button : buttons)
        {
            int textColor = SEC_FOREGROUND_COLOR;
            int backgroundColor = BACKGROUND_COLOR;

            StringBuilder text = new StringBuilder();

            Mod mod = button.mod;
            text.append(mod.name);

            if (Arrays.toString(Category.values()).contains(mod.name)) // category
            {
                backgroundColor = FOREGROUND_COLOR;
            }
            else if (mod.enabled) {
                textColor = FOREGROUND_COLOR;

                if (mod.modes != null) {
                    text.append(String.format(" [%s] ", mod.modes.get(mod.mode)));
                }
            }

            context.fill(button.x, button.y, button.x + button.width, button.y + button.height, backgroundColor);
            context.drawHorizontalLine(button.x, button.x + button.width - 1, button.y, FOREGROUND_COLOR);
            context.drawVerticalLine(button.x, button.y, button.y + button.height, FOREGROUND_COLOR);
            context.drawVerticalLine(button.x + button.width - 1, button.y, button.y + button.height, FOREGROUND_COLOR);
            context.drawHorizontalLine(button.x, button.x + button.width - 1, button.y + button.height, FOREGROUND_COLOR);
            context.drawText(textRenderer, text.toString(),button.x + 6, button.y + 6, textColor, true);

            /*if (clickedOn(button, mouseX, mouseY)) {
                List<Text> description = Arrays.asList(
                        Text.literal("This is a test description"),
                        Text.literal("So cool!"),
                        Text.literal("WoW!")
                );
                context.drawTooltip(textRenderer, description, mouseX, mouseY + 5);
            }*/
        }


        this.getFocused();

        super.render(context, mouseX, mouseY, delta);
    }



    @Override
    public boolean mouseClicked (double mouseX, double mouseY, int clickedButton) {
        for (CustomButton button : buttons) {
            if (clickedOn(button, mouseX, mouseY))
            {
                if (button.mod.modes == null) {
                    button.mod.enabled = !button.mod.enabled;
                } else {
                    button.mod.mode += 1;
                    if (button.mod.mode >= button.mod.modes.size()) {
                        button.mod.mode = -1;
                        button.mod.enabled = false;
                    } else {
                        button.mod.enabled = true;
                    }
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
