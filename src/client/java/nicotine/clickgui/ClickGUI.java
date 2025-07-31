package nicotine.clickgui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import nicotine.clickgui.guibutton.CategoryButton;
import nicotine.clickgui.guibutton.GUIButton;
import nicotine.clickgui.guibutton.ModButton;
import nicotine.clickgui.guibutton.OptionButton;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.util.ColorUtil;
import nicotine.util.Settings;
import nicotine.util.render.RenderGUI;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.mc;

public class ClickGUI extends Screen {

    public static Vector2f guiPos = new Vector2f(-1, -1);

    private static final Vector2i guiSize = new Vector2i(-1, -1);
    private static final Vector2i guiPosDragOffset = new Vector2i(-1, -1);

    private static CategoryButton selectedCategory = null;
    private static ModButton selectedModButton = null;

    private static KeybindOption keybindOptionToSet = null;

    private static final HashMap<CategoryButton, List<ModButton>> buttons = new HashMap<>();
    private static final List<OptionButton> optionButtons = new ArrayList<>();

    public static boolean showDescription = false;
    public static boolean blur = false;

    public ClickGUI() {
        super(Text.literal("nicotine GUI"));
    }

    private void initGuiPos() {
        guiPos.x = 0.5f;
        guiPos.y = 0.5f;
    }

    private void setGuiSize() {
        int categoryWidthSum = 0;
        int modCountHighest = 0;

        for (ModCategory modCategory : ModManager.modules.keySet()) {
            int categoryWidth = mc.textRenderer.getWidth(modCategory.name());
            categoryWidthSum += categoryWidth + 10;

            List<Mod> mods = ModManager.modules.get(modCategory);

            if (mods.size() > modCountHighest) {
                modCountHighest = mods.size();
            }
        }

        guiSize.x = categoryWidthSum + 50;
        guiSize.y = modCountHighest * (mc.textRenderer.fontHeight + 7) + 20;
    }

    private void getOptionButtons() {
        optionButtons.clear();

        final int fontHeight = mc.textRenderer.fontHeight;

        Vector2i pos = RenderGUI.relativePosToAbsPos(guiPos, guiSize);

        int guiY = pos.y + fontHeight + 15;

        Mod mod = selectedModButton.mod;

        for (ModOption modOption : mod.modOptions) {
            guiY += fontHeight + 7;
            int modWidth = mc.textRenderer.getWidth(modOption.name);
            int posX = (pos.x + guiSize.x / 2) + 10;

            if (modOption instanceof SwitchOption switchOption) {
                modWidth +=  mc.textRenderer.getWidth(switchOption.value + " []");
            } else if (modOption instanceof SliderOption) {
                modWidth = guiSize.x / 2 - 11;
            }

            OptionButton optionButton = new OptionButton(posX, guiY, modWidth, fontHeight + 5, mod, modOption);
            optionButtons.add(optionButton);
        }
    }

    private void getButtons() {
        buttons.clear();
        setGuiSize();

        final int fontHeight =  mc.textRenderer.fontHeight;

        Vector2i pos = RenderGUI.relativePosToAbsPos(guiPos, guiSize);

        int guiX = pos.x + 30;

        for (ModCategory modCategory : ModManager.modules.keySet()) {

            int guiY = pos.y + 5;

            int categoryWidth =  mc.textRenderer.getWidth(modCategory.name());
            CategoryButton categoryButton = new CategoryButton(guiX, guiY, categoryWidth, fontHeight, modCategory.name());

            if (selectedCategory == null || selectedCategory.text.equals(categoryButton.text)) {
                selectedCategory = categoryButton;
            }

            List<ModButton> modButtons = new ArrayList<>();

            List<Mod> mods = ModManager.modules.get(modCategory);

            guiY += 3;

            for (Mod mod : mods)
            {
                guiY += fontHeight + 7;
                ModButton modButton = new ModButton(pos.x + 5, guiY, (guiSize.x / 2) - 6, fontHeight + 5, mod);
                modButtons.add(modButton);

                if (selectedModButton == null) {
                    selectedModButton = new ModButton(modButton);
                }
            }

            buttons.put(categoryButton, modButtons);

            guiX += categoryWidth + 10;
        }

        getOptionButtons();
    }

    private String formatKeybind(String keybind, KeybindOption keybindOption) {
        String keyBindText = "";
        String[] splitKeybind = keybind.split(" ");
        if (splitKeybind.length >= 2) {
            for (String kbText : splitKeybind) {
                keyBindText += kbText.substring(0, 1);
            }
        } else {
            keyBindText = keybind;
        }

        if (keybindOptionToSet == keybindOption)
            keyBindText = "_";

        return keyBindText;
    }

    private void drawGUI(DrawContext context, double mouseX, double mouseY) {
        final int fontHeight = textRenderer.fontHeight;

        Vector2i pos = RenderGUI.relativePosToAbsPos(guiPos, guiSize);

        int dynColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

        context.fill(pos.x, pos.y, pos.x + guiSize.x, pos.y + guiSize.y, ColorUtil.BACKGROUND_COLOR);

        RenderGUI.drawBorder(context, pos.x, pos.y, guiSize.x, guiSize.y, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal()));

        RenderGUI.drawBorder(context, pos.x, pos.y, guiSize.x, fontHeight + 9, dynColor);


        for (CategoryButton button : buttons.keySet()) {
            int categoryColor = ColorUtil.FOREGROUND_COLOR;

            if (button == selectedCategory) {
                categoryColor = ColorUtil.ACTIVE_FOREGROUND_COLOR;
            }

            Text text = Text.of(button.text);

            if (mouseOver(button, mouseX, mouseY)) {
                text = text.getWithStyle(text.getStyle().withItalic(true)).getFirst();
            }

            context.drawText(textRenderer, text, button.x, button.y, categoryColor, true);
        }

        for (ModButton button : buttons.get(selectedCategory)) {

            int modColor = ColorUtil.FOREGROUND_COLOR;

            if (button.mod.enabled || button.mod.alwaysEnabled) {
                modColor = ColorUtil.ACTIVE_FOREGROUND_COLOR;
            }

            if (button.mod == selectedModButton.mod) {
                context.fill(button.x - 4, button.y - 5, button.x + (guiSize.x / 2) - 5, button.y + button.height, ColorUtil.SELECTED_BACKGROUND_COLOR);
                RenderGUI.drawBorderHorizontal(context, button.x - 4, button.y - 6, (guiSize.x / 2) - 2, button.height + 5, dynColor);
            }

            String text = button.mod.name;

            if (mouseOver(button, mouseX, mouseY)) {
                text = " " + text;
            }

            if (showDescription) {
                List<Text> description = new ArrayList<>();
                String[] splitDescription = button.mod.description.split("\n");
                for (int j = 0; j < splitDescription.length; j++) {
                    description.add(Text.of(splitDescription[j]));
                    if (j + 1 < splitDescription.length) {
                        description.add(Text.of(""));
                    }
                }

                if (mouseOver(button, mouseX, mouseY) && !button.mod.description.isBlank()) {
                    context.drawTooltip(mc.textRenderer, description, (int) mouseX + 3, (int) mouseY + splitDescription.length * 3);
                }
            }

            context.drawText(textRenderer, text, button.x, button.y, modColor, true);
        }

        int lineY = pos.y + fontHeight + 9;
        context.drawVerticalLine((pos.x + guiSize.x / 2) - 1, lineY, lineY + (guiSize.y - fontHeight - 9), dynColor);

        ModButton firstModButton = buttons.get(selectedCategory).getFirst(); // we just use this for the position
        selectedModButton.x = (pos.x + guiSize.x / 2) + 5;
        selectedModButton.y = firstModButton.y;
        selectedModButton.width = mc.textRenderer.getWidth("Enabled");

        Text selectedModText = Text.of("Enabled");

        if (mouseOver(selectedModButton, mouseX, mouseY)) {
            selectedModText = selectedModText.getWithStyle(selectedModText.getStyle().withItalic(true)).getFirst();
        }

        context.drawText(textRenderer, selectedModText, selectedModButton.x, selectedModButton.y, selectedModButton.mod.enabled || selectedModButton.mod.alwaysEnabled ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR, true);

        for (OptionButton button : optionButtons) {
            ModOption modOption = button.modOption;

            int posX = button.x;
            int posY = button.y;

            String text = null;
            int optionColor = ColorUtil.ACTIVE_FOREGROUND_COLOR;

            if (modOption instanceof SwitchOption switchOption) {
                text = String.format("%s [%s]", switchOption.name, switchOption.value);
            }  else if (modOption instanceof SliderOption sliderOption) {
                text = String.format("%s [%.2f]", sliderOption.name, sliderOption.value);
                //text = sliderOption.name;
                //float percent = sliderOption.value / sliderOption.maxValue;
                //context.drawHorizontalLine(optionButton.x, optionButton.x + (int)(optionButton.width * percent), optionButton.y + (fontHeight / 2), ColorUtil.ACTIVE_FOREGROUND_COLOR);
            } else if (modOption instanceof ToggleOption toggleOption) {
                if (!toggleOption.enabled) {
                    optionColor = ColorUtil.FOREGROUND_COLOR;
                }

                text = toggleOption.name;
            } else if (modOption instanceof KeybindOption keybindOption) {
                String keybind = InputUtil.fromKeyCode(keybindOption.keyCode, 0).getLocalizedText().getString();
                String keyBindText = formatKeybind(keybind, keybindOption);
                text = String.format("%s [%s]", keybindOption.name, keyBindText);
            }

            Text formattedText = Text.of(text);

            if (mouseOver(button, mouseX, mouseY)) {
                formattedText = formattedText.getWithStyle(formattedText.getStyle().withItalic(true)).getFirst();
            }

            context.drawText(textRenderer, formattedText, posX, posY, optionColor, true);
        }
    }

    private boolean mouseOver(GUIButton button, double mouseX, double mouseY) {
        return (button.x <= mouseX && mouseX <= button.x + button.width && button.y <= mouseY && mouseY <= button.y + button.height);
    }

    private boolean mouseOver(int posX, int posY, int width, int height, double mouseX, double mouseY) {
        return (posX <= mouseX && mouseX <= posX + width && posY <= mouseY && mouseY <= posY + height);
    }

    private void setSliderOption(SliderOption sliderOption, GUIButton button, double mouseX) {
        float value = sliderOption.minValue + ((((float) (Math.round(mouseX) - button.x) / button.width)) * (sliderOption.maxValue - sliderOption.minValue));
        value = Math.round(value * 100.0f) / 100.0f;

        if (sliderOption.decimal) {
            sliderOption.value = value;
        } else {
            sliderOption.value =  Math.round(value);
        }
    }

    private void setSwitchOption(SwitchOption switchOption) {
        for (int i = 0; i < switchOption.modes.length; i++) {
            if (switchOption.value.equals(switchOption.modes[i])) {
                if (i+1 <= switchOption.modes.length - 1) {
                    switchOption.value = switchOption.modes[i+1];
                } else {
                    switchOption.value = switchOption.modes[0];
                }
                break;
            }
        }
    }

    @Override
    public void init() {
        if (guiPos.x == -1 && guiPos.y == -1) {
            initGuiPos();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            Settings.save();
            this.close();
        }

        if (keybindOptionToSet != null) {
            keybindOptionToSet.keyCode = keyCode;
            keybindOptionToSet = null;
        }

        return true;
    }

    @Override
    public boolean mouseClicked (double mouseX, double mouseY, int clickedButton) {
        if (mouseOver(selectedModButton, mouseX, mouseY)) {
            if (clickedButton == 0 && !selectedModButton.mod.alwaysEnabled) {
                selectedModButton.mod.toggle();
                return true;
            }
        }

        for (CategoryButton categoryButton : buttons.keySet()) {
            if (mouseOver(categoryButton, mouseX, mouseY))  {
                if (clickedButton == 0) {
                    selectedCategory = categoryButton;
                    selectedModButton = buttons.get(categoryButton).getFirst();
                }

                return true;
            }
        }

        for (ModButton modButton : buttons.get(selectedCategory)) {
            if (mouseOver(modButton, mouseX, mouseY))  {
                if (clickedButton == 0) {
                    selectedModButton = modButton;
                }

                return true;
            }
        }

        for (OptionButton optionButton : optionButtons) {
            if (mouseOver(optionButton, mouseX, mouseY)) {
                if (clickedButton == 0) {
                    if (optionButton.modOption instanceof SwitchOption switchOption) {
                        setSwitchOption(switchOption);
                    } else if (optionButton.modOption instanceof SliderOption sliderOption) {
                        setSliderOption(sliderOption, optionButton, mouseX);
                    } else if (optionButton.modOption instanceof ToggleOption toggleOption) {
                        toggleOption.enabled = !toggleOption.enabled;
                    } else if (optionButton.modOption instanceof KeybindOption keybindOption) {
                        keybindOptionToSet = keybindOption;
                    }
                }

                return true;
            }
        }

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (guiPosDragOffset.x != -1 && guiPosDragOffset.y != -1) {
            guiPosDragOffset.x = -1;
            guiPosDragOffset.y = -1;
        }

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (OptionButton optionButton : optionButtons) {
            if (mouseOver(optionButton, mouseX, mouseY))  {
                if (optionButton.modOption instanceof SliderOption sliderOption) {
                    setSliderOption(sliderOption, optionButton, mouseX);
                    return true;
                }
            }
        }

        if (guiPosDragOffset.x != -1 && guiPosDragOffset.y != -1) {

            final int width = mc.getWindow().getScaledWidth();
            final int height = mc.getWindow().getScaledHeight();

            int posX = (int) mouseX + guiPosDragOffset.x;
            int posY = (int) mouseY + guiPosDragOffset.y;

            if (posX < 0) {
                posX = 0;
            } else if (posX + guiSize.x >= width) {
                posX = width - guiSize.x - 1;
            }

            if (posY < 0) {
                posY = 0;
            } else if (posY + guiSize.y >= height) {
                posY = height - guiSize.y - 1;
            }

            guiPos = RenderGUI.absPosToRelativePos(new Vector2i(posX, posY), guiSize);
            return true;
        }

        Vector2i pos = RenderGUI.relativePosToAbsPos(guiPos, guiSize);

        if (mouseOver(pos.x, pos.y, guiSize.x, mc.textRenderer.fontHeight + 9, mouseX, mouseY)) {
            guiPosDragOffset.x = pos.x - (int) mouseX;
            guiPosDragOffset.y = pos.y - (int) mouseY;
        }

        return true;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (blur) {
            this.applyBlur(context);
            this.renderDarkening(context);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        getButtons();
        super.render(context, mouseX, mouseY, delta);
        drawGUI(context, mouseX, mouseY);
    }

}
