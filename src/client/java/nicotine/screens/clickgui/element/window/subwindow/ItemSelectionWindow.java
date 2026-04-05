package nicotine.screens.clickgui.element.window.subwindow;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import nicotine.mod.option.ItemSelectionOption;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.screens.clickgui.element.button.InputText;
import nicotine.screens.clickgui.element.button.ItemButton;
import nicotine.screens.clickgui.element.misc.HLine;
import nicotine.screens.clickgui.element.window.DecoratedWindow;
import nicotine.util.ColorUtil;

import java.util.*;

import static nicotine.util.Common.*;

public class ItemSelectionWindow extends DecoratedWindow {
    private final ItemSelectionOption selectionOption;
    public static boolean builtContents = false;

    private final InputText inputText;

    private final List<ItemStack> items = new ArrayList<>();

    public ItemSelectionWindow(BaseScreen screen, String title, ItemSelectionOption selectionOption) {
        super(screen, title, 0, 0, 224, 180);

        this.selectionOption = selectionOption;

        if (!builtContents) {
            buildItemGroups();
            builtContents = true;
        }

        inputText = new InputText(0, 0, this.width - 4, mc.font.lineHeight + 2, "Search");

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

        inputText.x = posX - 2;
        inputText.y = posY - 2;

        this.add(inputText);

        posX -= 3;
        posY += mc.font.lineHeight + 5;

        List<ItemStack> filteredItems = items.stream().filter(
                x -> x.getHoverName().getString().toLowerCase().contains(inputText.text.toLowerCase())
        ).toList();


        if (this.width / 17 * this.scrollOffset > filteredItems.size() - 1 && !filteredItems.isEmpty()) {
            scrollOffset--;
        }

        int skipAmount = this.width / 17 * this.scrollOffset;

        for (ItemStack itemStack : filteredItems.stream().skip(skipAmount).toList()) {
            if (posX + 17 > this.x + this.width) {
                posX = this.x + 2;
                posY += 17;
            }

            if (posY + 17 > this.y + this.height) {
                break;
            }

            ItemButton itemButton = new ItemButton(itemStack, selectionOption, posX, posY);
            this.add(itemButton);

            posX += 17;
        }
    }
}
