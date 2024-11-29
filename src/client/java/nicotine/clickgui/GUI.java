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
import nicotine.util.Colors;
import nicotine.util.RenderGUI;
import nicotine.util.Settings;

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

    private int guiYInitPos = 10;
    private boolean saveSettings = false;
    private KeybindOption keybindOptionToSet = null;

    private void getButtons() {
        buttons.clear();

        int guiY = guiYInitPos;
        int guiX = 10;

        int height = mc.textRenderer.fontHeight + 10;
        int width = 85;

        for (HashMap.Entry<ModCategory, List<Mod>> modSet : ModManager.modules.entrySet()) {

            String categoryText = modSet.getKey().toString();
            CategoryButton categoryButton = new CategoryButton(guiX, guiY, width, height, categoryText);
            guiY += height;

            buttons.add(categoryButton);

            for (Mod mod : modSet.getValue())
            {
                ModButton modButton = new ModButton(guiX, guiY, width, height, mod);
                guiY += height;
                buttons.add(modButton);

                if (mod.optionsVisible) {
                    for (ModOption modOption : mod.modOptions) {
                        OptionButton optionButton = new OptionButton(guiX, guiY, width, height, mod, modOption);
                        guiY += height;
                        buttons.add(optionButton);
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
        final int watermarkPosX = mc.getWindow().getScaledWidth()  - textRenderer.getWidth(watermarkText) - 10;
        final int watermarkPosY = mc.getWindow().getScaledHeight() - textRenderer.fontHeight - 10;
        context.drawText(textRenderer, watermarkText,  watermarkPosX , watermarkPosY, Colors.rainbow, true);
    }

    private void drawButtons(DrawContext context) {
        getButtons();

        for (CustomButton button : buttons) {
            int textColor = Colors.FOREGROUND_COLOR;
            int backgroundColor = Colors.BACKGROUND_COLOR;
            int xOffSet, yOffset;
            xOffSet = yOffset = 6;

            String text = "";

           if (button instanceof CategoryButton categoryButton) {
               backgroundColor = Colors.CATEGORY_BACKGROUND_COLOR;
               textColor = Colors.CATEGORY_FOREGROUND_COLOR;
               text = categoryButton.text;

           } else if (button instanceof ModButton modButton) {
               if (modButton.mod.enabled || modButton.mod.alwaysEnabled) {
                   textColor = Colors.ACTIVE_FOREGROUND_COLOR;
                }
               text = modButton.mod.name;

           } else if (button instanceof OptionButton optionButton) {
               ModOption modOption = optionButton.modOption;

               if (optionButton.mod.enabled || optionButton.mod.alwaysEnabled) {
                   textColor = Colors.ACTIVE_FOREGROUND_COLOR;
               }

               if (modOption instanceof SwitchOption switchOption) {
                   text = String.format("%s [%s]", switchOption.name, switchOption.value);
               }  else if (modOption instanceof SliderOption sliderOption) {
                   text = String.format("%s [%.2f]", sliderOption.name, sliderOption.value);
               } else if (modOption instanceof ToggleOption toggleOption) {
                   if (toggleOption.enabled) {
                       textColor = Colors.ACTIVE_FOREGROUND_COLOR;
                   } else {
                       textColor = Colors.FOREGROUND_COLOR;
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
            RenderGUI.drawBorder(context, button.x, button.y, button.width, button.height, Colors.BORDER_COLOR);
            context.drawText(textRenderer, text, button.x + xOffSet, button.y + yOffset, textColor, true);
        }
    }

    private boolean clickedOn(CustomButton button, double mouseX, double mouseY) {
        return (button.x <= mouseX && mouseX <= button.x + button.width && button.y <= mouseY && mouseY <= button.y + button.height);
    }

    private void setSliderOption(SliderOption sliderOption, CustomButton button, double mouseX) {
        float value = sliderOption.minValue + ((((float) (Math.round(mouseX) - button.x) / button.width)) * (sliderOption.maxValue - sliderOption.minValue));
        value =  Math.round(value * 100.0f) / 100.0f;

        if ((int)sliderOption.minValue == sliderOption.minValue) {
            sliderOption.value =  Math.round(value);
        } else {
            sliderOption.value = value;
        }
    }

    private void setSwitchOption(SwitchOption switchOption) {
        for (int i = 0; i<=switchOption.modes.length - 1; i++) {
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
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (saveSettings) {
            Settings.save();
            saveSettings = false;
        }

        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawWatermark(context);
        drawButtons(context);

        super.render(context, mouseX, mouseY, delta);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            this.close();
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

        return false;
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (CustomButton customButton : buttons) {
            if (clickedOn(customButton, mouseX, mouseY))  {
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
            if (clickedOn(button, mouseX, mouseY))  {
                if (button instanceof ModButton modButton) {
                    if (clickedButton == 1) {
                        modButton.mod.optionsVisible = !modButton.mod.optionsVisible;
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
