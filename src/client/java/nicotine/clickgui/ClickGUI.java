package nicotine.clickgui;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import nicotine.clickgui.guibutton.*;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.util.ColorUtil;
import nicotine.util.Settings;
import nicotine.util.render.GUI;
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

    public static Vector2i pos = new Vector2i(0, 0);
    private static Vector2i size = new Vector2i(0, 0);

    public static boolean showDescription = false;
    public static boolean blur = false;


    public ClickGUI() {
        super(Text.of("nicotine GUI"));
    }

    private static boolean mouseOverButton(GUIButton button, double mouseX, double mouseY) {
        return (button.x <= mouseX && mouseX <= button.x + button.width && button.y <= mouseY && mouseY <= button.y + button.height);
    }

    private String keyCodeToString(int keyCode) {
        if (keyCode == -1)
            return "";
        else if (keyCode < 8) {
            return switch (keyCode) {
                case InputUtil.GLFW_MOUSE_BUTTON_LEFT -> "MBL";
                case InputUtil.GLFW_MOUSE_BUTTON_RIGHT -> "MBR";
                case InputUtil.GLFW_MOUSE_BUTTON_MIDDLE -> "MBM";
                default -> String.format("MB%d", keyCode);
            };
        } else {
            return InputUtil.fromKeyCode(new KeyInput(keyCode, 0, 0)).getLocalizedText().getString();
        }
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

    private static void setGUIPos() {
        pos.x = (mc.getWindow().getScaledWidth() / 2) - (size.x / 2);
        pos.y = (mc.getWindow().getScaledHeight() / 2) - (size.y / 2);
    }

    private static void getCategoryButtons() {
        categoryButtons.clear();

        int posX = pos.x + (PADDING * 2);
        int posY = pos.y + 4;

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

        if (selectedCategory == null) {
            selectedCategory = ModCategory.values()[0];
            selectedMod = ModManager.modules.get(selectedCategory).getFirst();
        }

        int posX = pos.x + PADDING;
        int posY = categoryButtons.getFirst().y + categoryButtons.getFirst().height + PADDING;

        for (Mod mod : ModManager.modules.get(selectedCategory)) {
            ModButton modButton = new ModButton(
                    posX,
                    posY,
                    (size.x / 2) - PADDING,
                    mc.textRenderer.fontHeight + 3,
                    mod
            );

            modButtons.add(modButton);

            posY += mc.textRenderer.fontHeight + 5;
        }
    }

    private void getOptionButtons() {
        optionButtons.clear();

        int posX = pos.x + size.x / 2 + PADDING;
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
                    modOption.subOption ? posX + PADDING : posX,
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
                        (size.x / 2) - optionButton.width - (PADDING * (modOption.subOption ? 3 : 2)) - 6,
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
                String keybindOptionString = String.format("%s [%s]", keybindOption.name, formatKeybind(keyCodeToString(keybindOption.keyCode), keybindOption));
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
        int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

        CategoryButton firstCategoryButton = categoryButtons.getFirst();
        int dividerLinePosY = firstCategoryButton.y + firstCategoryButton.height + 2;

        context.fill(pos.x, pos.y, pos.x + size.x, pos.y + size.y, ColorUtil.BACKGROUND_COLOR);
        GUI.drawBorder(context, pos.x, pos.y, size.x, size.y, dynamicColor);
        context.drawVerticalLine(pos.x + size.x / 2, dividerLinePosY, pos.y + size.y, dynamicColor);

        context.drawHorizontalLine(pos.x, pos.x + size.x, dividerLinePosY, dynamicColor);

        for (CategoryButton categoryButton : categoryButtons) {
            int categoryColor = mouseOverButton(categoryButton, mouseX, mouseY) || categoryButton.text.equals(selectedCategory.name()) ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR;

            context.drawText(mc.textRenderer, categoryButton.text, categoryButton.x, categoryButton.y, categoryColor, true);
        }

        for (int i = 0; i < modButtons.size(); i++) {
            ModButton modButton = modButtons.get(i);
            String buttonText = modButton.mod.name;

            int modColor = modButton.mod.enabled || modButton.mod.alwaysEnabled ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR;

            if (selectedMod.name.equals(modButton.mod.name)) {
                context.fill(
                        modButton.x - PADDING + 1,
                        modButton.y - 3,
                        modButton.x + modButton.width,
                        modButton.y + modButton.height + 1,
                        ColorUtil.SELECTED_BACKGROUND_COLOR
                );

                GUI.drawBorderHorizontal(
                        context,
                        modButton.x - PADDING + 1,
                        modButton.y - 3,
                        modButton.width + PADDING - 2,
                        modButton.height + 3,
                        dynamicColor
                );
            }

            if (mouseOverButton(modButton, mouseX, mouseY)) {
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

                GUI.drawBorder(
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
                GUI.drawBorder(context, sliderPosX, sliderButton.sliderY, 3, sliderButton.height - 2, ColorUtil.BORDER_COLOR);

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
                optionButtonText = String.format("%s [%s]", keybindOption.name, formatKeybind(keyCodeToString(keybindOption.keyCode), keybindOption));
            } else if (optionButton.modOption instanceof ToggleOption toggleOption) {
                optionColor = toggleOption.enabled ? ColorUtil.ACTIVE_FOREGROUND_COLOR : ColorUtil.FOREGROUND_COLOR;
            }

            context.drawText(mc.textRenderer, optionButtonText, optionButton.x, optionButton.y, optionColor, true);
        }
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == InputUtil.GLFW_KEY_ESCAPE) {
            Settings.save();
            this.close();
        }

        if (keybindOptionToSet != null) {
            keybindOptionToSet.keyCode = input.key();
            keybindOptionToSet = null;
        }

        return true;
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {

        if (keybindOptionToSet != null) {
            keybindOptionToSet.keyCode = click.getKeycode();
            keybindOptionToSet = null;

            return true;
        }

        if (click.getKeycode() != InputUtil.GLFW_MOUSE_BUTTON_LEFT)
            return true;

        double mouseX = click.x();
        double mouseY = click.y();

        for (GUIButton guiButton : Stream.concat(Stream.concat(categoryButtons.stream(), modButtons.stream()), optionButtons.stream()).toList()) {
            if (!mouseOverButton(guiButton, mouseX, mouseY))
                continue;

            if (guiButton instanceof CategoryButton categoryButton) {
                selectedCategory = ModCategory.valueOf(categoryButton.text);
                selectedMod = ModManager.modules.get(selectedCategory).getFirst();

                break;
            } else if (guiButton instanceof ModButton modButton) {
                selectedMod = modButton.mod;

                break;
            } else if (guiButton instanceof OptionButton optionButton) {
                if (optionButton.modOption instanceof ToggleOption toggleOption) {

                    if (toggleOption.name.equals("Enabled") && !selectedMod.alwaysEnabled) {
                        selectedMod.toggle();
                    } else {
                        toggleOption.enabled = !toggleOption.enabled;
                    }

                    break;
                } else if (optionButton.modOption instanceof SwitchOption switchOption) {
                    setSwitchOption(switchOption);

                    break;
                } else if (optionButton.modOption instanceof KeybindOption keybindOption) {
                    keybindOptionToSet = keybindOption;

                    break;
                } else if (optionButton.modOption instanceof SliderOption sliderOption) {
                    SliderButton sliderButton = (SliderButton) optionButton;

                    if (!GUI.mouseOver(sliderButton.sliderX, sliderButton.sliderY, sliderButton.width, sliderButton.height, mouseX, mouseY))
                        break;

                    setSliderOption(sliderOption, sliderButton, mouseX);


                    break;
                }
            }
        }

        return true;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        double mouseX = click.x();
        double mouseY = click.y();

        for (OptionButton optionButton : optionButtons) {
            if (optionButton.modOption instanceof SliderOption sliderOption) {
                SliderButton sliderButton = (SliderButton) optionButton;

                if (GUI.mouseOver(sliderButton.sliderX, sliderButton.sliderY, sliderButton.sliderWidth, sliderButton.sliderHeight, mouseX, mouseY))  {
                    setSliderOption(sliderOption, sliderButton, mouseX);
                    return true;
                }
            }
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
        setGUIPos();
        getCategoryButtons();
        getModButtons();
        getOptionButtons();

        super.render(context, mouseX, mouseY, delta);

        drawGUI(context, mouseX, mouseY);
    }
}
