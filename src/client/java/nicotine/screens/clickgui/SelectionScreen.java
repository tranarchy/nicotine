package nicotine.screens.clickgui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import nicotine.mod.mods.gui.GUI;
import nicotine.mod.option.SelectionOption;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.InputText;
import nicotine.screens.clickgui.element.button.ItemButton;
import nicotine.screens.clickgui.element.misc.HLine;
import nicotine.util.ColorUtil;

import java.util.*;

import static nicotine.util.Common.*;

public class SelectionScreen extends BaseScreen {
    private SelectionOption selectionOption;

    public SelectionScreen(SelectionOption selectionOption) {
        super("Item selection screen", new Window(0, 0, 0, 0));

        FeatureFlagSet featureFlagSet = Optional.ofNullable(mc.player).map(x -> x.connection).map(ClientPacketListener::enabledFeatures).orElse(FeatureFlagSet.of());
        CreativeModeTab.ItemDisplayParameters itemDisplayParameters = new CreativeModeTab.ItemDisplayParameters(featureFlagSet, true, mc.level.registryAccess());

        for (CreativeModeTab itemGroup : CreativeModeTabs.allTabs()) {
            if (itemGroup.getSearchTabDisplayItems().isEmpty())
                itemGroup.buildContents(itemDisplayParameters);
        }

        this.selectionOption = selectionOption;
        window.width = GUI.screen.window.width;
        window.height = GUI.screen.window.height;
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

    @Override
    protected void addDrawables() {
        int posX = window.x + 5;
        int posY = window.y + 5;

        window.add(
                new HLine(
                        window.x,
                        posY + mc.font.lineHeight + 2,
                        window.width,
                        ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal())
                )
        );

        InputText inputText = new InputText(posX, posY, window.width - 5, mc.font.lineHeight + 2);

        window.add(inputText);

        posY += 16;

        for (ItemStack itemStack : getAllItems()) {
            if (!itemStack.getHoverName().getString().toLowerCase().contains(InputText.text))
                continue;

            if (posX + 16 > window.x + window.width) {
                posX = window.x + 5;
                posY += 16;
            }

            if (posY + 16 > window.y + window.height) {
                break;
            }

            ItemButton itemButton = new ItemButton(itemStack, selectionOption, posX, posY);
            window.add(itemButton);

            posX += 16;
        }
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.key() == InputConstants.KEY_ESCAPE) {
            InputText.reset();
            mc.setScreen(GUI.screen);
        } else {
            super.keyPressed(keyEvent);
        }

        return true;
    }
}
