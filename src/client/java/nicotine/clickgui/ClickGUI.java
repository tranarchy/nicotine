package nicotine.clickgui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import nicotine.clickgui.guibutton.*;
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
import java.util.List;
import java.util.stream.Stream;

import static nicotine.util.Common.*;

public class ClickGUI extends Screen {

    private static final int PADDING = 5;

    private static List<CategoryButton> categoryButtons = new ArrayList<>();
    private static List<ModButton> modButtons = new ArrayList<>();
    private static List<OptionButton> optionButtons = new ArrayList<>();

    private static KeybindOption keybindOptionToSet = null;

    private static Mod selectedMod = null;
    private static ModCategory selectedCategory = null;

    public static Vector2f pos = new Vector2f(0.5f, 0.5f);
    private static Vector2i dragOffset = new Vector2i(-1, -1);
    private static Vector2i size = new Vector2i(0, 0);

    public static boolean showDescription = false;
    public static boolean blur = false;

    public ClickGUI() {
        super(Text.of("nicotine GUI"));
    }

    private static boolean mouseOver(GUIButton button, double mouseX, double mouseY) {
        return (button.x <= mouseX && mouseX <= button.x + button.width && button.y <= mouseY && mouseY <= button.y + button.height);
    }

    private boolean mouseOver(int posX, int posY, int width, int height, double mouseX, double mouseY) {
        return (posX <= mouseX && mouseX <= posX + width && posY <= mouseY && mouseY <= posY + height);
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

    private static void setGUISize() {
        int highestModCount = 0;

        size.x = 14 + (PADDING * ModCategory.values().length);

        for (ModCategory modCategory : ModCategory.values()) {
            int modCount = ModManager.modules.get(modCategory).size();

            if (modCount > highestModCount) {
                highestModCount = modCount;
            }

            size.x += mc.textRenderer.getWidth(modCategory.name()) + PADDING;
        }

        size.x -= PADDING;

        size.y = (highestModCount + 1) * (mc.textRenderer.fontHeight + PADDING) + 2;
    }

    private static void getCategoryButtons() {
        categoryButtons.clear();

        Vector2i absPos = RenderGUI.relativePosToAbsPos(pos, size);

        int posX = absPos.x + (PADDING * 2);
        int posY = absPos.y + 4;

        for (ModCategory modCategory : ModCategory.values()) {
            CategoryButton categoryButton = new CategoryButton(
                    posX,
                    posY,
                    mc.textRenderer.getWidth(modCategory.name()),
                    mc.textRenderer.fontHeight,
                    modCategory.name()
            );

            categoryButtons.add(categoryButton);

            posX += categoryButton.width + (PADDING * 2);
        }
    }

    private static void getModButtons() {
        modButtons.clear();

        Vector2i absPos = RenderGUI.relativePosToAbsPos(pos, size);

        if (selectedCategory == null) {
            selectedCategory = ModCategory.values()[0];
            selectedMod = ModManager.modules.get(selectedCategory).getFirst();
        }

        int posX = absPos.x + PADDING;
        int posY = categoryButtons.getFirst().y + categoryButtons.getFirst().height + PADDING;

        for (Mod mod : ModManager.modules.get(selectedCategory)) {
            ModButton modButton = new ModButton(
                    posX,
                    posY,
                    size.x / 2,
                    mc.textRenderer.fontHeight + 3,
                    mod
            );

            modButtons.add(modButton);

            posY += mc.textRenderer.fontHeight + 5;
        }
    }

    private void getOptionButtons() {
        optionButtons.clear();

        Vector2i absPos = RenderGUI.relativePosToAbsPos(pos, size);

        int posX = absPos.x + size.x / 2 + PADDING;
        int posY = categoryButtons.getFirst().y + categoryButtons.getFirst().height + PADDING;

        ToggleOption toggleModOption = new ToggleOption("Enabled", selectedMod.enabled);
        OptionButton toggleModOptionButton = new OptionButton(
                posX,
                posY,
                mc.textRenderer.getWidth("Enabled"),
                mc.textRenderer.fontHeight + 3,
                selectedMod,
                toggleModOption
        );

        optionButtons.add(toggleModOptionButton);

        posX += PADDING;
        posY += mc.textRenderer.fontHeight + 5;

        for (ModOption modOption : selectedMod.modOptions) {
            OptionButton optionButton = new OptionButton(
                    posX,
                    posY,
                    mc.textRenderer.getWidth(modOption.name),
                    mc.textRenderer.fontHeight + 3,
                    selectedMod,
                    modOption
            );

            if (modOption instanceof SliderOption) {
                SliderButton sliderButton = new SliderButton(
                        optionButton.x,
                        optionButton.y,
                        optionButton.width,
                        optionButton.height,
                        optionButton.x + optionButton.width + 3,
                        optionButton.y - 2,
                        (size.x / 2) - optionButton.width - (PADDING * 2) - 6,
                        optionButton.height - 2,
                        optionButton.mod,
                        optionButton.modOption
                );

                optionButtons.add(sliderButton);
            } else if (modOption instanceof SwitchOption switchOption) {
                String switchOptionString = String.format("%s [%s]", switchOption.name, switchOption.value);
                optionButton.width = mc.textRenderer.getWidth(switchOptionString);

                optionButtons.add(optionButton);
            } else if (modOption instanceof KeybindOption keybindOption) {
                String keybind = InputUtil.fromKeyCode(keybindOption.keyCode, 0).getLocalizedText().getString();
                String keybindOptionString = String.format("%s [%s]", keybindOption.name, formatKeybind(keybind, keybindOption));
                optionButton.width = mc.textRenderer.getWidth(keybindOptionString);

                optionButtons.add(optionButton);
            } else {
                optionButtons.add(optionButton);
            }

            posY += mc.textRenderer.fontHeight + 5;
        }
    }

    private void setSwitchOption(SwitchOption switchOption) {
        for (int i = 0; i < switchOption.modes.length; i++) {
            if (switchOption.value.equals(switchOption.modes[i])) {
                switchOption.value = switchOption.modes[i+1 < switchOption.modes.length ? i+1 : 0];
                break;
            }
        }
    }

    private void setSliderOption(SliderOption sliderOption, SliderButton button, double mouseX) {
        float value = sliderOption.minValue + ((((float) (Math.round(mouseX) - button.sliderX) / button.sliderWidth)) * (sliderOption.maxValue - sliderOption.minValue));
        value = Math.round(value * 100.0f) / 100.0f;

        if (sliderOption.decimal) {
            sliderOption.value = value;
        } else {
            sliderOption.value =  Math.round(value);
        }
    }

    private void drawGUI(DrawContext context, int mouseX, int mouseY) {
        Vector2i absPos = RenderGUI.relativePosToAbsPos(pos, size);

        int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

        CategoryButton firstCategoryButton = categoryButtons.getFirst();
        int dividerLinePosY = firstCategoryButton.y + firstCategoryButton.height + 2;

        context.fill(absPos.x, absPos.y, absPos.x + size.x, absPos.y + size.y, ColorUtil.BACKGROUND_COLOR);
        RenderGUI.drawBorder(context, absPos.x, absPos.y, size.x, size.y, dynamicColor);
        context.drawVerticalLine(absPos.x + size.x / 2, dividerLinePosY, absPos.y + size.y, dynamicColor);

        context.drawHorizontalLine(absPos.x, absPos.x + size.x, dividerLinePosY, dynamicColor);

        for (CategoryButton categoryButton : categoryButtons) {
            int categoryColor = mouseOver(categoryButton, mouseX, mouseY) ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR;

            context.drawText(mc.textRenderer, categoryButton.text, categoryButton.x, categoryButton.y, categoryColor, true);
        }

        for (int i = 0; i < modButtons.size(); i++) {
            ModButton modButton = modButtons.get(i);
            String buttonText = modButton.mod.name;

            int modColor = modButton.mod.enabled || modButton.mod.alwaysEnabled ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR;

            if (selectedMod.name.equals(modButton.mod.name)) {
                context.fill(modButton.x - PADDING + 1,  modButton.y - 3, modButton.x - PADDING + modButton.width, modButton.y + modButton.height + 1, ColorUtil.SELECTED_BACKGROUND_COLOR);
                RenderGUI.drawBorderHorizontal(context, modButton.x - PADDING + 1, modButton.y - 3, modButton.width - 2, modButton.height + 3, dynamicColor);
            }

            if (mouseOver(modButton, mouseX, mouseY)) {
                buttonText = " " + buttonText;


                if (showDescription && !modButton.mod.description.isBlank()) {
                    List<Text> description = new ArrayList<>();
                    String[] splitDescription = modButton.mod.description.split("\n");
                    for (int j = 0; j < splitDescription.length; j++) {
                        description.add(Text.of(splitDescription[j]));
                        if (j + 1 < splitDescription.length) {
                            description.add(Text.of(""));
                        }
                    }

                    context.drawTooltip(mc.textRenderer, description, mouseX + 3, mouseY + splitDescription.length * 3);
                }
            }

            context.drawText(mc.textRenderer, buttonText, modButton.x, modButton.y, modColor,true);
        }

        for (OptionButton optionButton : optionButtons) {

            int optionColor =  ColorUtil.FOREGROUND_COLOR;

            String optionButtonText = optionButton.modOption.name;

            if (optionButton.modOption instanceof SliderOption sliderOption) {
               SliderButton sliderButton = (SliderButton)optionButton;

                context.fill(
                        sliderButton.sliderX,
                        sliderButton.sliderY,
                        sliderButton.sliderX + sliderButton.sliderWidth,
                        sliderButton.sliderY + sliderButton.height - 1,
                        ColorUtil.SELECTED_BACKGROUND_COLOR
                );

                RenderGUI.drawBorder(
                        context,
                        sliderButton.sliderX,
                        sliderButton.sliderY,
                        sliderButton.sliderWidth,
                        sliderButton.sliderHeight,
                        ColorUtil.BORDER_COLOR
                );

                float normalizedSliderValue = (sliderOption.value - sliderOption.minValue) / (sliderOption.maxValue - sliderOption.minValue);
                int sliderPosX = (int) (sliderButton.sliderX + (sliderButton.sliderWidth * normalizedSliderValue));

                if (sliderOption.value == sliderOption.maxValue) {
                    sliderPosX -= 3;
                }

                context.fill(sliderPosX,sliderButton.sliderY, sliderPosX + 3, sliderButton.sliderY + sliderButton.height - 1, ColorUtil.ACTIVE_FOREGROUND_COLOR);
                RenderGUI.drawBorder(context, sliderPosX, sliderButton.sliderY, 3, sliderButton.height - 2, ColorUtil.BORDER_COLOR);

                String sliderText = sliderOption.decimal ? Float.toString(sliderOption.value) : Integer.toString((int)sliderOption.value);

                context.drawText(
                        mc.textRenderer,
                        Text.of(sliderText),
                        (sliderButton.sliderX + (sliderButton.sliderWidth  / 2)) - (mc.textRenderer.getWidth(sliderText) / 2),
                        sliderButton.y,
                        ColorUtil.FOREGROUND_COLOR,
                        true
                );
            } else if (optionButton.modOption instanceof SwitchOption switchOption) {
                optionButtonText = String.format("%s [%s]", switchOption.name, switchOption.value);
            } else if (optionButton.modOption instanceof KeybindOption keybindOption) {
                String keybind = InputUtil.fromKeyCode(keybindOption.keyCode, 0).getLocalizedText().getString();
                optionButtonText = String.format("%s [%s]", keybindOption.name, formatKeybind(keybind, keybindOption));
            } else if (optionButton.modOption instanceof ToggleOption toggleOption) {
                optionColor = toggleOption.enabled ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR;
            }

            context.drawText(mc.textRenderer, optionButtonText, optionButton.x, optionButton.y, optionColor, true);
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
        if (clickedButton != 0)
            return true;

        for (GUIButton guiButton : Stream.concat(Stream.concat(categoryButtons.stream(), modButtons.stream()), optionButtons.stream()).toList()) {
            if (!mouseOver(guiButton, mouseX, mouseY))
                continue;

            if (guiButton instanceof CategoryButton categoryButton) {
                selectedCategory = ModCategory.valueOf(categoryButton.text);
                selectedMod = ModManager.modules.get(selectedCategory).getFirst();

                return true;
            } else if (guiButton instanceof ModButton modButton) {
                selectedMod = modButton.mod;

                return true;
            } else if (guiButton instanceof OptionButton optionButton) {
                if (optionButton.modOption instanceof ToggleOption toggleOption) {

                    if (toggleOption.name.equals("Enabled") && !selectedMod.alwaysEnabled) {
                        selectedMod.enabled = !selectedMod.enabled;
                    } else {
                        toggleOption.enabled = !toggleOption.enabled;
                    }

                    return true;
                } else if (optionButton.modOption instanceof SwitchOption switchOption) {
                    setSwitchOption(switchOption);

                    return true;
                } else if (optionButton.modOption instanceof KeybindOption keybindOption) {
                    keybindOptionToSet = keybindOption;

                    return true;
                } else if (optionButton.modOption instanceof SliderOption sliderOption) {
                    setSliderOption(sliderOption, (SliderButton) optionButton, mouseX);

                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (OptionButton optionButton : optionButtons) {
            if (optionButton.modOption instanceof SliderOption sliderOption) {
                SliderButton sliderButton = (SliderButton) optionButton;

                if (mouseOver(sliderButton.sliderX, sliderButton.sliderY, sliderButton.sliderWidth, sliderButton.sliderHeight, mouseX, mouseY))  {
                    setSliderOption(sliderOption, sliderButton, mouseX);
                    return true;
                }
            }
        }

        if (dragOffset.x != -1 && dragOffset.y != -1) {

            final int width = mc.getWindow().getScaledWidth();
            final int height = mc.getWindow().getScaledHeight();

            int posX = (int) mouseX + dragOffset.x;
            int posY = (int) mouseY + dragOffset.y;

            if (posX < 0) {
                posX = 0;
            } else if (posX + size.x >= width) {
                posX = width - size.x - 1;
            }

            if (posY < 0) {
                posY = 0;
            } else if (posY + size.y >= height) {
                posY = height - size.y - 1;
            }

            pos = RenderGUI.absPosToRelativePos(new Vector2i(posX, posY), size);
            return true;
        }

        Vector2i absPos = RenderGUI.relativePosToAbsPos(pos, size);

        if (mouseOver(absPos.x, absPos.y, size.x, mc.textRenderer.fontHeight + 9, mouseX, mouseY)) {
            dragOffset.x = absPos.x - (int) mouseX;
            dragOffset.y = absPos.y - (int) mouseY;
        }

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (dragOffset.x != -1 && dragOffset.y != -1) {
            dragOffset.x = -1;
            dragOffset.y = -1;
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
        setGUISize();
        getCategoryButtons();
        getModButtons();
        getOptionButtons();

        super.render(context, mouseX, mouseY, delta);

        drawGUI(context, mouseX, mouseY);
    }
}
