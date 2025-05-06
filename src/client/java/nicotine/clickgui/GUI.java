package nicotine.clickgui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.clickgui.custombutton.CategoryButton;
import nicotine.clickgui.custombutton.CustomButton;
import nicotine.clickgui.custombutton.ModButton;
import nicotine.clickgui.custombutton.OptionButton;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.util.ColorUtil;
import nicotine.util.render.RenderGUI;
import nicotine.util.Settings;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.nicotine;

public class GUI extends Screen {
    public GUI() {
        super(Text.literal("nicotine GUI"));
    }

    private final List<CustomButton> buttons = new ArrayList<>();

    public static HashMap<ModCategory, Vector2d> guiPos = new HashMap<>();

    private int guiYInitPos = 8;
    private boolean saveSettings = false;
    private KeybindOption keybindOptionToSet = null;

    private String searchString = "";
    private boolean searchMode = false;

    public static boolean showDescription = false;
    public static boolean blur = false;

    private void getButtons() {
        buttons.clear();

        int guiY = guiYInitPos;
        int guiX = 8;

        int height = mc.textRenderer.fontHeight + 10;
        int width = 85;

        for (HashMap.Entry<ModCategory, List<Mod>> modSet : ModManager.modules.entrySet()) {

            String categoryText = modSet.getKey().toString();
            CategoryButton categoryButton = new CategoryButton(guiX, guiY, width, height, categoryText);
            guiY += height;

            buttons.add(categoryButton);

            for (Mod mod : modSet.getValue())
            {
                if (!mod.name.toLowerCase().contains(searchString))
                    continue;

                ModButton modButton = new ModButton(guiX, guiY, width, height, mod);
                guiY += height;
                buttons.add(modButton);

                if (mod.optionsVisible) {
                    for (ModOption modOption : mod.modOptions) {
                        OptionButton optionButton = new OptionButton(guiX, guiY, width, height, mod, modOption);
                        buttons.add(optionButton);

                        guiY += height;
                    }
                }
            }

            guiX += width + 15;
            guiY = guiYInitPos;

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


    private void drawWatermark(DrawContext context) {
        final String watermarkText =  String.format("nicotine %sv%s", Formatting.WHITE, nicotine.getVersion());
        final int watermarkPosX = mc.getWindow().getScaledWidth() - textRenderer.getWidth(watermarkText) - 8;
        final int watermarkPosY = mc.getWindow().getScaledHeight() - textRenderer.fontHeight - 8;
        context.drawText(textRenderer, watermarkText,  watermarkPosX , watermarkPosY, ColorUtil.rainbow, true);
    }

    private void drawButtons(DrawContext context, double mouseX, double mouseY) {
        for (int i = 0; i < buttons.size(); i++) {
            int textColor = ColorUtil.FOREGROUND_COLOR;
            int backgroundColor = ColorUtil.BACKGROUND_COLOR;
            int xOffSet, yOffset;
            xOffSet = yOffset = 6;

           String text = "";

           CustomButton button = buttons.get(i);

           if (button instanceof CategoryButton categoryButton) {
               backgroundColor = ColorUtil.CATEGORY_BACKGROUND_COLOR;
               textColor = ColorUtil.CATEGORY_FOREGROUND_COLOR;
               text = categoryButton.text;

           } else if (button instanceof ModButton modButton) {
               if (modButton.mod.enabled || modButton.mod.alwaysEnabled) {
                   textColor = ColorUtil.ACTIVE_FOREGROUND_COLOR;
                }
               text = modButton.mod.name;

               if (showDescription) {
                   List<Text> description = new ArrayList<>();
                   String[] splitDescription = modButton.mod.description.split("\n");
                   for (int j = 0; j < splitDescription.length; j++) {
                       description.add(Text.of(splitDescription[j]));
                       if (j + 1 < splitDescription.length) {
                           description.add(Text.of(""));
                       }
                   }

                   if (mouseOver(button, mouseX, mouseY) && !modButton.mod.description.isBlank()) {
                       context.drawTooltip(mc.textRenderer, description, (int) mouseX + 3, (int) mouseY + splitDescription.length * 3);
                   }
               }

           } else if (button instanceof OptionButton optionButton) {
               ModOption modOption = optionButton.modOption;

               if (optionButton.mod.enabled || optionButton.mod.alwaysEnabled) {
                   textColor = ColorUtil.ACTIVE_FOREGROUND_COLOR;
               }

               if (modOption instanceof SwitchOption switchOption) {
                   text = String.format("%s [%s]", switchOption.name, switchOption.value);
               }  else if (modOption instanceof SliderOption sliderOption) {
                   text = String.format("%s [%.2f]", sliderOption.name, sliderOption.value);
               } else if (modOption instanceof ToggleOption toggleOption) {
                   if (toggleOption.enabled) {
                       textColor = ColorUtil.ACTIVE_FOREGROUND_COLOR;
                   } else {
                       textColor = ColorUtil.FOREGROUND_COLOR;
                   }
                   text = toggleOption.name;
               } else if (modOption instanceof KeybindOption keybindOption) {
                   String keybind = InputUtil.fromKeyCode(keybindOption.keyCode, 0).getLocalizedText().getString();
                   String keyBindText = formatKeybind(keybind, keybindOption);
                   text = String.format("%s [%s]", keybindOption.name, keyBindText);
               }

               xOffSet = 10;
           }


            context.fill(button.x, button.y, button.x + button.width, button.y + button.height, backgroundColor);

           if (button instanceof CategoryButton) {
               RenderGUI.drawBorder(context, button.x, button.y, button.width, button.height, ColorUtil.ACTIVE_FOREGROUND_COLOR);
           } else if (i + 1 < buttons.size()) {
               if (buttons.get(i + 1) instanceof CategoryButton) {
                   context.drawHorizontalLine(button.x, button.x + button.width, button.y + button.height, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.dynamicBrightnessVal));
                   RenderGUI.drawBorderVertical(context, button.x, button.y - 1, button.width, button.height + 1, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.dynamicBrightnessVal));
               } else {
                   RenderGUI.drawBorderHorizontal(context, button.x, button.y, button.width, button.height, ColorUtil.BORDER_COLOR);
                   RenderGUI.drawBorderVertical(context, button.x, button.y - 1, button.width, button.height + 1, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.dynamicBrightnessVal));
               }
           } else {
               context.drawHorizontalLine(button.x, button.x + button.width, button.y + button.height, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.dynamicBrightnessVal));
               RenderGUI.drawBorderVertical(context, button.x, button.y - 1, button.width, button.height + 1, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.dynamicBrightnessVal));
           }

           context.drawText(textRenderer, text, button.x + xOffSet, button.y + yOffset, textColor, true);
        }
    }

    private void drawSearchString(DrawContext context) {
        if (searchMode) {
            getButtons();
            String formattedSearchString = String.format("Searching for %s%s", Formatting.WHITE, searchString);
            final int x = (mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(formattedSearchString) / 2);
            final int y = mc.getWindow().getScaledHeight() - 80;
            context.drawText(mc.textRenderer, formattedSearchString, x, y, ColorUtil.ACTIVE_FOREGROUND_COLOR, true);
        }
    }

    private boolean mouseOver(CustomButton button, double mouseX, double mouseY) {
        return (button.x <= mouseX && mouseX <= button.x + button.width && button.y <= mouseY && mouseY <= button.y + button.height);
    }

    private void setSliderOption(SliderOption sliderOption, CustomButton button, double mouseX) {
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
        getButtons();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (saveSettings) {
            Settings.save();
            saveSettings = false;
        }

        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (blur) {
            this.applyBlur();
            this.renderDarkening(context);
        }

        drawSearchString(context);
        drawWatermark(context);
        drawButtons(context, mouseX, mouseY);
    }

    public static boolean isLetter(int keyCode) {
        return (keyCode >= InputUtil.GLFW_KEY_A && keyCode <= InputUtil.GLFW_KEY_Z);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            if (searchMode) {
                searchMode = false;
                searchString = "";
                getButtons();
            } else {
                this.close();
            }
            return true;
        }

        if (searchMode) {
            if (keyCode == InputUtil.GLFW_KEY_BACKSPACE && !searchString.isBlank()) {
                searchString = searchString.substring(0, searchString.length() - 1);
            } else if (isLetter(keyCode)) {
                searchString += InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText().getString().toLowerCase();
            }
            return true;
        }

        if (keyCode == InputUtil.GLFW_KEY_SLASH) {
            searchMode = true;
            return true;
        }

        if (keybindOptionToSet != null) {
            keybindOptionToSet.keyCode = keyCode;
            keybindOptionToSet = null;
            Settings.save();
        }

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount < 0) {
            guiYInitPos -= 5;
        } else {
            guiYInitPos += 5;
        }

        getButtons();

        return false;
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (CustomButton customButton : buttons) {
            if (mouseOver(customButton, mouseX, mouseY))  {
                if (customButton instanceof OptionButton optionButton) {
                    if (optionButton.modOption instanceof SliderOption sliderOption) {
                        setSliderOption(sliderOption, customButton, mouseX);
                        saveSettings = true;
                        break;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean mouseClicked (double mouseX, double mouseY, int clickedButton) {
        for (CustomButton button : buttons) {
            if (mouseOver(button, mouseX, mouseY))  {
                if (button instanceof ModButton modButton) {
                    if (clickedButton == 1) {
                        modButton.mod.optionsVisible = !modButton.mod.optionsVisible;
                        getButtons();
                    } else if (clickedButton == 0) {
                        if (!modButton.mod.alwaysEnabled) {
                            modButton.mod.toggle();
                        }
                    }
                } else if (button instanceof OptionButton optionButton) {
                    if (clickedButton != 0)
                        break;

                    if (optionButton.modOption instanceof SwitchOption switchOption) {
                        setSwitchOption(switchOption);
                    } else if (optionButton.modOption instanceof SliderOption sliderOption) {
                        setSliderOption(sliderOption, button, mouseX);
                    } else if (optionButton.modOption instanceof ToggleOption toggleOption) {
                        toggleOption.enabled = !toggleOption.enabled;
                    } else if (optionButton.modOption instanceof KeybindOption keybindOption) {
                        keybindOptionToSet = keybindOption;
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
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {}

}
