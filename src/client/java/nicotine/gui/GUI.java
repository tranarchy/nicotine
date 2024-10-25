package nicotine.gui;

import nicotine.util.Settings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.minecraftClient;
import static nicotine.util.Modules.*;

public class GUI extends Screen {
    public GUI() {
        super(Text.literal("nicotine"));
    }

    public static final int PRIMARY_FG = 0xFFFFFFFF;
    public static final int SECONDARY_FG = 0xFFA796FB;
    public static final int BACKGROUND = 0x80000000;

    private final List<CustomButton> buttons = new ArrayList<>();

    private void getButtons() {
        buttons.clear();

        int guiY = 10;
        int guiX = 10;

        int height = minecraftClient.textRenderer.fontHeight + 10;
        int width = 82;

        for (HashMap.Entry<String, List<Mod>> modSet : modList.entrySet()) {

            Mod category = new Mod();
            category.name = modSet.getKey();
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
        getButtons();

        for (CustomButton button : buttons)
        {
            int textColor = PRIMARY_FG;
            int backgroundColor = BACKGROUND;

            StringBuilder text = new StringBuilder();

            text.append(button.mod.name);

            if (modList.containsKey(button.mod.name)) // category
            {
                backgroundColor = SECONDARY_FG;
            }
            else if (button.mod.enabled) {
                textColor = SECONDARY_FG;

                if (button.mod.modes != null) {
                    text.append(" [");
                    text.append(button.mod.modes.get(button.mod.mode));
                    text.append("]");
                }
            }

            context.fill(button.x, button.y, button.x + button.width, button.y + button.height, backgroundColor);
            context.drawBorder(button.x, button.y, button.width, button.height, SECONDARY_FG);
            context.drawText(textRenderer, text.toString(),button.x + 6, button.y + 6, textColor, true);

            /*if (clickedOn(button, mouseX, mouseY)) {
                String description = "This is a description";
                context.fill(mouseX + 3, mouseY - 2, mouseX + 7 + textRenderer.getWidth(description), mouseY + textRenderer.fontHeight + 2, 0xFF000000);
                context.drawText(textRenderer, description, mouseX + 5, mouseY, PRIMARY_FG, true);
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
                    Settings.save();
                    break;
                }


                button.mod.mode += 1;
                if (button.mod.mode >= button.mod.modes.size()) {
                    button.mod.mode = -1;
                    button.mod.enabled = false;
                } else
                    button.mod.enabled = true;

                Settings.save();

                break;
            }
        }

        return true;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
       return;
    }

}
