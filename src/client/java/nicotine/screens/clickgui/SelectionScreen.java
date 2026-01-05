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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nicotine.mod.option.SelectionOption;
import nicotine.screens.clickgui.guibutton.ItemButton;
import nicotine.util.ColorUtil;
import nicotine.util.Settings;
import nicotine.util.render.GUI;
import org.joml.Vector2i;

import java.util.*;

import static nicotine.util.Common.*;

public class SelectionScreen extends Screen {
    private SelectionOption selectionOption;
    private String searchString = "";

    private static final int PADDING = 5;

    private static Vector2i pos = new Vector2i(0, 0);
    private static Vector2i size = new Vector2i(0, 0);

    public SelectionScreen(SelectionOption selectionOption) {
        super(Component.literal("Selection screen"));

        FeatureFlagSet featureFlagSet = Optional.ofNullable(mc.player).map(x -> x.connection).map(ClientPacketListener::enabledFeatures).orElse(FeatureFlagSet.of());
        CreativeModeTab.ItemDisplayParameters itemDisplayParameters = new CreativeModeTab.ItemDisplayParameters(featureFlagSet, true, mc.level.registryAccess());

        for (CreativeModeTab itemGroup : CreativeModeTabs.allTabs()) {
            itemGroup.buildContents(itemDisplayParameters);
        }

        this.selectionOption = selectionOption;
        size = ClickGUI.size;
    }

    public List<ItemStack> getAllItems() {
        List<ItemStack> items = new ArrayList<>();

        for (CreativeModeTab itemGroup : CreativeModeTabs.allTabs()) {
            for (ItemStack itemStack : itemGroup.getSearchTabDisplayItems()) {
                if (!items.contains(itemStack))
                    items.add(itemStack);
            }
        }

        return items;
    }

    public List<ItemButton> getItemButtons() {
        List<ItemButton> itemButtons = new ArrayList<>();

        int itemPosX = pos.x + 5;
        int itemPosY = pos.y + 5;

        itemPosY += 16;

        for (ItemStack itemStack : getAllItems()) {
            if (!itemStack.getHoverName().getString().toLowerCase().contains(searchString))
                continue;

            if (itemPosX + 16 > pos.x + size.x) {
                itemPosX = pos.x + 5;
                itemPosY += 16;
            }

            if (itemPosY + 16 > pos.y + size.y) {
                break;
            }

            ItemButton itemButton = new ItemButton(itemPosX, itemPosY, 16, 16, itemStack);
            itemButtons.add(itemButton);

            itemPosX += 16;
        }

        return itemButtons;
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.key() == InputConstants.KEY_ESCAPE) {
            Settings.save();
            this.onClose();
        } else if (keyEvent.key() == InputConstants.KEY_SPACE) {
            searchString += " ";
        } else if (keyEvent.key() == InputConstants.KEY_BACKSPACE) {
            searchString = searchString.substring(0, searchString.length() - 1);
        } else {
            String input = InputConstants.getKey(new KeyEvent(keyEvent.key(), 0, 0)).getDisplayName().getString().toLowerCase();

            if (input.length() < 2 && mc.font.width(searchString + input + "_") < size.x) {
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

        for (ItemButton itemButton : getItemButtons()) {
            if (GUI.mouseOver(itemButton.x, itemButton.y, itemButton.width, itemButton.height, mouseX, mouseY)) {
                Item item = itemButton.itemStack.getItem();

                if (selectionOption.items.contains(item)) {
                    System.out.println("contains");
                    selectionOption.items.remove(item);
                } else {
                    selectionOption.items.add(item);
                }
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
        GUI.centerPosition(pos, size);

        int posX = pos.x + 5;
        int posY = pos.y + 4;

        int dividerLinePosY = posY + mc.font.lineHeight + 2;

        int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

        context.fill(pos.x, pos.y, pos.x + size.x, pos.y + size.y, ColorUtil.BACKGROUND_COLOR);
        GUI.drawBorder(context, pos.x, pos.y, size.x, size.y, dynamicColor);
        context.hLine(pos.x, pos.x + size.x, dividerLinePosY, dynamicColor);

        context.drawString(mc.font, searchString + "_", posX, posY, ColorUtil.FOREGROUND_COLOR, true);

        for (ItemButton itemButton : getItemButtons()) {
            if (selectionOption.items.contains(itemButton.itemStack.getItem()))
                context.fill(itemButton.x, itemButton.y, itemButton.x + itemButton.width, itemButton.y + itemButton.height, ColorUtil.FOREGROUND_COLOR);

            if (GUI.mouseOver(itemButton.x, itemButton.y, itemButton.width, itemButton.height, mouseX, mouseY))
               GUI.drawBorder(context, itemButton.x, itemButton.y, itemButton.width, itemButton.height, ColorUtil.ACTIVE_FOREGROUND_COLOR);

            context.renderFakeItem(itemButton.itemStack, itemButton.x, itemButton.y);
        }
    }

}
