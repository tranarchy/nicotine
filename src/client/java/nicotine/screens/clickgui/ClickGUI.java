package nicotine.screens.clickgui;

import nicotine.screens.clickgui.element.Element;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.*;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.screens.clickgui.element.misc.HLine;
import nicotine.screens.clickgui.element.misc.VLine;
import nicotine.util.ColorUtil;

import static nicotine.util.Common.*;

public class ClickGUI extends BaseScreen {

    private final int PADDING = 5;

    public ClickGUI() {
        super("nicotine GUI", new Window(0, 0, 0, 0));
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
        int posY = window.elements.getFirst().y + window.elements.getFirst().height + PADDING;

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
        int posY = window.elements.getFirst().y + window.elements.getFirst().height + PADDING;

        if (!ModButton.selectedMod.alwaysEnabled) {
            ToggleOption toggleModOption = new ToggleOption("Enabled", ModButton.selectedMod.enabled) {
                @Override
                public void toggle() {
                    ModButton.selectedMod.toggle();
                }
            };

            ToggleButton toggleModOptionButton = new ToggleButton(
                    toggleModOption,
                    posX,
                    posY
            );

            window.elements.add(toggleModOptionButton);

            posX += PADDING;
            posY += mc.font.lineHeight + 5;
        }

        for (ModOption modOption : ModButton.selectedMod.modOptions) {
            Element element = new Element(
                    modOption.subOption ? posX + PADDING : posX,
                    posY,
                    mc.font.width(modOption.name),
                    mc.font.lineHeight + 3
            );

            if (modOption instanceof SliderOption sliderOption) {
                SliderButton sliderButton = new SliderButton(
                        sliderOption,
                        element.x,
                        element.y,
                        element.x + element.width + 3,
                        element.y - 2,
                        (window.width / 2) - element.width - (PADDING * (modOption.subOption ? 3 : 2)) - 6,
                        element.height - 2
                );

                window.add(sliderButton);
            } else if (modOption instanceof DropDownOption dropDownOption) {
                DropDownButton dropDownButton = new DropDownButton(dropDownOption, element.x, element.y);
                window.add(dropDownButton);
            } else if (modOption instanceof KeybindOption keybindOption) {
                KeybindButton keybindButton = new KeybindButton(keybindOption, element.x, element.y);
                window.add(keybindButton);
            } else if (modOption instanceof ToggleOption toggleOption) {
                ToggleButton toggleButton = new ToggleButton(toggleOption, element.x, element.y);
                window.add(toggleButton);
            } else if (modOption instanceof ItemSelectionOption selectionOption) {
                String title = String.format("%s - Items", ModButton.selectedMod.name);
                ItemSelectionButton selectionButton = new ItemSelectionButton(title, selectionOption, element.x, element.y);
                window.add(selectionButton);
            } else if (modOption instanceof RGBOption rgbOption) {
                String title = String.format("%s - RGB", ModButton.selectedMod.name);
                RGBButton rgbButton = new RGBButton(title, rgbOption, element.x, element.y);
                window.add(rgbButton);
            }

            posY += mc.font.lineHeight + 5;
        }
    }

    @Override
    protected void addDrawables() {
        addCategoryButtons();
        addModButtons();
        addOptionButtons();

        Element firstElement = window.elements.getFirst();

        int dividerLinePosY = firstElement.y + firstElement.height + 2;

        window.add(new VLine(window.x + window.width / 2, dividerLinePosY, window.y + window.height - dividerLinePosY, ColorUtil.getPulsatingColor()));
        window.add(new HLine(window.x, dividerLinePosY, window.width, ColorUtil.getPulsatingColor()));

        super.addDrawables();
    }
}
