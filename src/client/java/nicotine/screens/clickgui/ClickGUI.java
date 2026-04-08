package nicotine.screens.clickgui;

import nicotine.screens.clickgui.element.button.*;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.screens.clickgui.element.misc.HLine;
import nicotine.screens.clickgui.element.misc.VLine;
import nicotine.screens.clickgui.element.window.DecoratedWindow;
import nicotine.util.ColorUtil;

import static nicotine.util.Common.*;

public class ClickGUI extends BaseScreen {

    private final int PADDING = 5;

    public ClickGUI() {
        super("nicotine GUI", new DecoratedWindow(null, String.format("nicotine v%s", nicotine.getVersion()), 0, 0, 0, 0));
        setWindowSize();
    }

    private void setWindowSize() {
        int highestModCount = 0;

        window.width = 14 + (PADDING * ModCategory.values().length);

        for (ModCategory modCategory : ModCategory.values()) {
            int modCount = ModManager.modules.get(modCategory).size();

            if (modCount > highestModCount) {
                highestModCount = modCount;
            }

            window.width += mc.font.width(modCategory.name()) + PADDING;
        }

        window.width -= PADDING;

        window.height = (highestModCount + 1) * (mc.font.lineHeight + PADDING) + 2;
    }

    private void addCategoryButtons() {
        int posX = window.x + (PADDING * 2);
        int posY = window.y + 4;

        for (ModCategory modCategory : ModCategory.values()) {
            CategoryButton categoryButton = new CategoryButton(
                    modCategory,
                    posX,
                    posY
            );

            window.add(categoryButton);

            posX += categoryButton.width + (PADDING * 2);
        }
    }

    private void addModButtons() {
        if (CategoryButton.selectedModCategory == null) {
            CategoryButton.selectedModCategory = ModCategory.values()[0];
        }

        int posX = window.x + PADDING;
        int posY = window.y + 4 + mc.font.lineHeight + PADDING;

        for (Mod mod : ModManager.modules.get(CategoryButton.selectedModCategory)) {
            if (ModButton.selectedMod == null) {
                ModButton.selectedMod = mod;
            }

            ModButton modButton = new ModButton(
                    mod,
                    posX,
                    posY
            );

            modButton.width = (window.width / 2) - PADDING;

            window.add(modButton);

            posY += mc.font.lineHeight + 5;
        }
    }

    private void addOptionButtons() {
        int posX = window.x + window.width / 2 + PADDING;
        int posY = window.y + 4 + mc.font.lineHeight + PADDING;

        if (!ModButton.selectedMod.alwaysEnabled) {
            ToggleOption toggleModOption = new ToggleOption("Enabled") {
                @Override
                public void toggle() {
                    ModButton.selectedMod.toggle();
                }
            };

            toggleModOption.enabled = ModButton.selectedMod.enabled;

            ToggleButton toggleModOptionButton = new ToggleButton(
                    toggleModOption,
                    posX,
                    posY
            );

            window.add(toggleModOptionButton);

            posX += PADDING;
            posY += mc.font.lineHeight + 5;
        }

        for (ModOption modOption : ModButton.selectedMod.modOptions) {
            
            int buttonX = posX + (modOption.subOption ? PADDING : 0);
            int buttonY = posY;
            
            int width = mc.font.width(modOption.name);
            int height = mc.font.lineHeight + 3;
            
            if (modOption instanceof SliderOption sliderOption) {
                SliderButton sliderButton = new SliderButton(
                        sliderOption,
                        buttonX,
                        buttonY,
                        buttonX + width + 3,
                        buttonY - 2,
                        (window.width / 2) - width - (PADDING * (modOption.subOption ? 3 : 2)) - 6,
                        height - 2
                );

                window.add(sliderButton);
            } else if (modOption instanceof DropDownOption dropDownOption) {
                DropDownButton dropDownButton = new DropDownButton(dropDownOption, buttonX, buttonY);
                window.add(dropDownButton);
            } else if (modOption instanceof KeybindOption keybindOption) {
                KeybindButton keybindButton = new KeybindButton(keybindOption, buttonX, buttonY);
                window.add(keybindButton);
            } else if (modOption instanceof ToggleOption toggleOption) {
                ToggleButton toggleButton = new ToggleButton(toggleOption, buttonX, buttonY);
                window.add(toggleButton);
            } else if (modOption instanceof ItemSelectionOption selectionOption) {
                String title = String.format("%s - %s", ModButton.selectedMod.name, selectionOption.name);
                ItemSelectionButton selectionButton = new ItemSelectionButton(this, title, selectionOption, buttonX, buttonY);
                window.add(selectionButton);
            } else if (modOption instanceof RGBOption rgbOption) {
                String title = String.format("%s - %s", ModButton.selectedMod.name, rgbOption.name);
                RGBButton rgbButton = new RGBButton(this, title, rgbOption, buttonX, buttonY);
                window.add(rgbButton);
            } else if (modOption instanceof ClickOption clickOption) {
                GUIButton guiButton = new GUIButton(clickOption.name, buttonX, buttonY) {
                    @Override
                    public void click(double mouseX, double mouseY, int input) {
                        clickOption.click();
                    }
                };
                window.add(guiButton);
            }

            posY += mc.font.lineHeight + 5;
        }
    }

    @Override
    protected void addDrawables() {
        addCategoryButtons();
        addModButtons();
        addOptionButtons();

        int dividerLinePosY = window.y + 4 + mc.font.lineHeight + 2;

        window.add(new VLine(window.x + window.width / 2, dividerLinePosY, window.y + window.height - dividerLinePosY, ColorUtil.getPulsatingColor()));
        window.add(new HLine(window.x, dividerLinePosY, window.width, ColorUtil.getPulsatingColor()));

        super.addDrawables();
    }
}
