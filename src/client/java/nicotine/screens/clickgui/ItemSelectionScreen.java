package nicotine.screens.clickgui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import nicotine.mod.mods.general.GUI;
import nicotine.mod.option.ItemSelectionOption;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.InputText;
import nicotine.screens.clickgui.element.button.ItemButton;
import nicotine.screens.clickgui.element.misc.HLine;
import nicotine.util.ColorUtil;

import java.util.*;

import static nicotine.util.Common.*;

public class ItemSelectionScreen extends BaseScreen {
    private ItemSelectionOption selectionOption;
    private static boolean builtContents = false;

    private final List<ItemStack> items = new ArrayList<>();

    public ItemSelectionScreen(ItemSelectionOption selectionOption) {
        super("Item selection screen", new Window(0, 0, 0, 0));

        this.selectionOption = selectionOption;
        window.width = GUI.screen.window.width;
        window.height = GUI.screen.window.height;

        if (!builtContents) {
            buildItemGroups();
            builtContents = true;
        }

        getAllItems();
    }

    private void buildItemGroups() {
        FeatureFlagSet featureFlagSet = Optional.ofNullable(mc.player).map(x -> x.connection).map(ClientPacketListener::enabledFeatures).orElse(FeatureFlagSet.of());
        CreativeModeTab.ItemDisplayParameters itemDisplayParameters = new CreativeModeTab.ItemDisplayParameters(featureFlagSet, true, mc.level.registryAccess());

        for (CreativeModeTab itemGroup : CreativeModeTabs.allTabs()) {
            if (itemGroup.getSearchTabDisplayItems().isEmpty())
                itemGroup.buildContents(itemDisplayParameters);
        }
    }

    public void getAllItems() {
        for (CreativeModeTab itemGroup : CreativeModeTabs.allTabs()) {
            for (ItemStack itemStack : itemGroup.getSearchTabDisplayItems()) {
                if (items.stream().noneMatch(x -> x.getItem().equals(itemStack.getItem())) &&
                        selectionOption.filter(itemStack.getItem()))
                    items.add(itemStack);
            }
        }
    }

    @Override
    protected void addDrawables() {
        int posX = window.x + 5;
        int posY = window.y + 5;

        InputText inputText = new InputText(posX, posY, window.width - 5, mc.font.lineHeight + 2);
        window.add(inputText);

        posY += mc.font.lineHeight + 2;

        HLine separator = new HLine(
                window.x,
                posY,
                window.width,
                ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal())
        );

        window.add(separator);

        posX -= 4;
        posY += 3;

        for (ItemStack itemStack : items) {
            if (!itemStack.getHoverName().getString().toLowerCase().contains(InputText.text))
                continue;

            if (posX + 16 > window.x + window.width) {
                posX = window.x + 1;
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
            if (InputText.captureInput) {
                InputText.captureInput = false;
            } else {
                InputText.reset();
                mc.setScreen(GUI.screen);
            }
        } else {
            super.keyPressed(keyEvent);
        }

        return true;
    }
}
