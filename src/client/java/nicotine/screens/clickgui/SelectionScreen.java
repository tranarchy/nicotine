package nicotine.screens.clickgui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import nicotine.mod.option.SelectionOption;
import nicotine.screens.clickgui.element.Element;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.ItemButton;
import nicotine.util.ColorUtil;
import nicotine.util.Settings;
import nicotine.util.render.GUI;

import java.util.*;

import static nicotine.util.Common.*;

public class SelectionScreen extends Screen {
    private SelectionOption selectionOption;
    private String searchString = "";

    private Window window;

    public SelectionScreen(SelectionOption selectionOption) {
        super(Component.literal("Selection screen"));

        FeatureFlagSet featureFlagSet = Optional.ofNullable(mc.player).map(x -> x.connection).map(ClientPacketListener::enabledFeatures).orElse(FeatureFlagSet.of());
        CreativeModeTab.ItemDisplayParameters itemDisplayParameters = new CreativeModeTab.ItemDisplayParameters(featureFlagSet, true, mc.level.registryAccess());

        for (CreativeModeTab itemGroup : CreativeModeTabs.allTabs()) {
            if (itemGroup.getSearchTabDisplayItems().isEmpty())
                itemGroup.buildContents(itemDisplayParameters);
        }

        this.selectionOption = selectionOption;
        window = new Window(0, 0, 0, 0);
        window.width = ClickGUI.window.width;
        window.height = ClickGUI.window.height;
    }

    public List<ItemStack> getAllItems() {
        List<ItemStack> items = new ArrayList<>();

        for (CreativeModeTab itemGroup : CreativeModeTabs.allTabs()) {
            for (ItemStack itemStack : itemGroup.getSearchTabDisplayItems()) {
                if (!items.contains(itemStack) && selectionOption.filter(itemStack.getItem()))
                    items.add(itemStack);
            }
        }

        return items;
    }

    public void addItemButtons() {
        int itemPosX = window.x + 5;
        int itemPosY = window.y + 5;

        itemPosY += 16;

        for (ItemStack itemStack : getAllItems()) {
            if (!itemStack.getHoverName().getString().toLowerCase().contains(searchString))
                continue;

            if (itemPosX + 16 > window.x + window.width) {
                itemPosX = window.x + 5;
                itemPosY += 16;
            }

            if (itemPosY + 16 > window.y + window.height) {
                break;
            }

            ItemButton itemButton = new ItemButton(itemStack, selectionOption, itemPosX, itemPosY);
            window.add(itemButton);

            itemPosX += 16;
        }
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.key() == InputConstants.KEY_ESCAPE) {
            Settings.save();
            this.onClose();
        } else if (keyEvent.key() == InputConstants.KEY_SPACE) {
            searchString += " ";
        } else if (keyEvent.key() == InputConstants.KEY_BACKSPACE && !searchString.isEmpty()) {
            searchString = searchString.substring(0, searchString.length() - 1);
        } else {
            String input = InputConstants.getKey(new KeyEvent(keyEvent.key(), 0, 0)).getDisplayName().getString().toLowerCase();

            if (input.length() < 2 && mc.font.width(searchString + input + "_") < window.width) {
                searchString += input;
            }
        }

        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubled) {
        if (mouseButtonEvent.input() != InputConstants.MOUSE_BUTTON_LEFT)
            return true;

        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        for (Element element : window.elements) {
            if (element instanceof ItemButton itemButton && GUI.mouseOver(element.x, element.y, element.width, element.height, mouseX, mouseY)) {
                itemButton.click(mouseX, mouseY);
            }
        }

        return true;
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (ClickGUI.blur) {
            this.renderBlurredBackground(context);
            this.renderMenuBackground(context);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        window.centerPosition();
        window.elements.clear();

        addItemButtons();

        window.draw(context, mouseX, mouseY);

        int posX = window.x + 5;
        int posY = window.y + 4;

        int dividerLinePosY = posY + mc.font.lineHeight + 2;

        int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

        context.hLine(window.x, window.x + window.width, dividerLinePosY, dynamicColor);

        context.drawString(mc.font, searchString + "_", posX, posY, ColorUtil.FOREGROUND_COLOR, true);
    }

}
