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
import nicotine.screens.clickgui.element.SubWindow;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.InputText;
import nicotine.screens.clickgui.element.button.ItemButton;
import nicotine.screens.clickgui.element.misc.HLine;
import nicotine.util.ColorUtil;

import java.util.*;

import static nicotine.util.Common.*;

public class ItemSelectionScreen extends SubWindow {
    private ItemSelectionOption selectionOption;
    public static boolean builtContents = false;

    private final InputText inputText;

    private final List<ItemStack> items = new ArrayList<>();

    public ItemSelectionScreen(BaseScreen screen, String title, ItemSelectionOption selectionOption) {
        super(screen, title, 0, 0, screen.window.width, screen.window.height);

        this.selectionOption = selectionOption;

        if (!builtContents) {
            buildItemGroups();
            builtContents = true;
        }

        inputText = new InputText(0, 0, this.width - 5, mc.font.lineHeight + 2);

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
    public void addDrawables() {
        super.addDrawables();

        int posX = this.x + 5;
        int posY = this.y + 5;

        inputText.x = posX;
        inputText.y = posY;

        this.add(inputText);

        posY += mc.font.lineHeight + 2;

        HLine separator = new HLine(this.x, posY, this.width, ColorUtil.getPulsatingColor());

        this.add(separator);

        posX -= 4;
        posY += 3;

        for (ItemStack itemStack : items) {
            if (!itemStack.getHoverName().getString().toLowerCase().contains(inputText.text))
                continue;

            if (posX + 16 > this.x + this.width) {
                posX = this.x + 1;
                posY += 16;
            }

            if (posY + 16 > this.y + this.height) {
                break;
            }

            ItemButton itemButton = new ItemButton(itemStack, selectionOption, posX, posY);
            this.add(itemButton);

            posX += 16;
        }
    }

    @Override
    public boolean handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.key() == InputConstants.KEY_ESCAPE && InputText.selectedTextBox != null) {
            InputText.selectedTextBox = null;
            return true;
        }

        return false;
    }
}
