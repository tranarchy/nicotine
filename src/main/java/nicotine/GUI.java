package nicotine;


import nicotine.util.Settings;
import nicotine.util.gui.Colors;
import nicotine.util.gui.CustomButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import nicotine.util.Common;
import nicotine.util.Module;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUI extends Screen {

    public GUI()
    {super(Text.literal("nicotine"));}

    private final List<CustomButton> buttons = new ArrayList<>();

    private void drawBorder(MatrixStack matrix, CustomButton button, int color) {
        drawHorizontalLine(matrix, button.x, button.x + button.width, button.y, color);
        drawVerticalLine(matrix, button.x, button.y + button.height, button.y, color);
        drawHorizontalLine(matrix, button.x, button.x + button.width, button.y + button.height, color);
        drawVerticalLine(matrix, button.x + button.width, button.y + button.height, button.y, color);
    }

    private void getButtons() {
        buttons.clear();

        int guiY = 10;
        int guiX = 10;

        int height = Common.mc.textRenderer.fontHeight + 10;
        int width = 82;

        for (HashMap.Entry<String, List<Module.Mod>> modSet : Module.modList.entrySet()) {

            Module.Mod category = new Module.Mod();
            category.name = modSet.getKey();
            CustomButton customButton = new CustomButton(guiX, guiY, width, height, category);
            guiY += height;

            buttons.add(customButton);

            for (Module.Mod mod : modSet.getValue())
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
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        getButtons();

        matrix.push();
        matrix.scale(1,1,1);

        for (CustomButton button : buttons)
        {
            int textColor = Colors.PRIMARY_FG;
            int backgroundColor = Colors.BACKGROUND;

            String text = button.mod.name;

            if (Module.modList.containsKey(button.mod.name)) // category
            {
                backgroundColor = Colors.SECONDARY_FG;
            }
            else if (button.mod.enabled) {
                textColor = Colors.SECONDARY_FG;
            }

            fill(matrix, button.x, button.y, button.x + button.width, button.y + button.height, backgroundColor);
            drawBorder(matrix, button, Colors.SECONDARY_FG);
            Common.mc.textRenderer.draw(matrix, text, button.x + 6, button.y + 6, textColor);
        }

        matrix.pop();
    }

    @Override
    public boolean mouseClicked (double mouseX, double mouseY, int clickedButton) {
        for (CustomButton button : buttons) {
            if (clickedOn(button, mouseX, mouseY))
            {

                button.mod.enabled = !button.mod.enabled;
                Settings.save();
            }
        }

        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
            Common.mc.setScreen(null);

        return true;
    }
}
