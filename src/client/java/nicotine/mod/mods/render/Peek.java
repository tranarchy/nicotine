package nicotine.mod.mods.render;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import nicotine.events.AbstractContainerScreenTickEvent;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.RenderTooltipEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.PeekScreen;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.Render2D;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class Peek extends Mod {
    public static boolean echestWasOpened = false;
    public static List<ItemStack> enderChestItems = new ArrayList<>();

    private final ToggleOption shulker = new ToggleOption("Shulker", true);
    private final ToggleOption enderChest = new ToggleOption("EnderChest");

    public Peek() {
        super(ModCategory.Render, "Peek", "Lets you see inside shulkers and echets without opening them");
        this.keybind.keyCode = InputConstants.KEY_LALT;
        this.keybind.name = "Inspect";
        this.addOptions(Arrays.asList(shulker, enderChest));
    }

    @Override
    protected void init() {
        final int SLOT_WIDTH = 18;
        final int MAX_WIDTH = SLOT_WIDTH * 9;

        EventBus.register(RenderTooltipEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (event.focusedSlot != null) {
                ItemStack focusedStack = event.focusedSlot.getItem();

                if ((focusedStack.is(ItemTags.SHULKER_BOXES) && shulker.enabled) || (focusedStack.getItem() == Items.ENDER_CHEST && enderChest.enabled) && echestWasOpened) {

                    List<ItemStack> itemsToPeek;

                    if (focusedStack.getItem() == Items.ENDER_CHEST) {
                        itemsToPeek = enderChestItems;
                    } else {
                        ItemContainerContents shulkerContainer = focusedStack.getComponents().get(DataComponents.CONTAINER);
                        itemsToPeek = shulkerContainer.allItemsCopyStream().toList();
                    }

                    if (InputConstants.isKeyDown(mc.getWindow(), keybind.keyCode)) {
                        SimpleContainer peekInventory = new SimpleContainer(9 * 3);

                        for (int i = 0; i < itemsToPeek.size(); i++) {
                            peekInventory.setItem(i, itemsToPeek.get(i));
                        }
                        mc.setScreen(new PeekScreen(focusedStack.getHoverName(), peekInventory));
                    }

                    int posX = event.x + (SLOT_WIDTH / 2);
                    int posY = event.y;

                    int itemIndex = 0;

                    Font textRenderer = mc.font;

                    ItemStack shulkerItem;

                    Matrix3x2fStack matrix3fStack = event.drawContext.pose().pushMatrix();

                    event.drawContext.pose().translate(0.0F, 0.0F, matrix3fStack.identity());

                    event.drawContext.fill(posX, posY, posX + MAX_WIDTH, posY + textRenderer.lineHeight + 4, ColorUtil.BACKGROUND_COLOR);
                    Render2D.drawBorder(event.drawContext, posX, posY, MAX_WIDTH, textRenderer.lineHeight + 4, ColorUtil.BORDER_COLOR);
                    event.drawContext.text(textRenderer, focusedStack.getHoverName(), posX + 3, posY + 3, ColorUtil.FOREGROUND_COLOR, true);

                    posY += textRenderer.lineHeight + 4;

                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 9; j++) {

                            shulkerItem = itemsToPeek.size() > itemIndex ? itemsToPeek.get(itemIndex) : ItemStack.EMPTY;
                            int stackCount = shulkerItem.getCount();

                            event.drawContext.fill(posX, posY, posX + SLOT_WIDTH, posY + SLOT_WIDTH, ColorUtil.BACKGROUND_COLOR);
                            Render2D.drawBorder(event.drawContext, posX, posY, SLOT_WIDTH, SLOT_WIDTH, ColorUtil.BORDER_COLOR);

                            event.drawContext.fakeItem(shulkerItem, posX + 1, posY + 1);
                            event.drawContext.itemDecorations(textRenderer, shulkerItem, posX + 1, posY + 1, stackCount > 1 ? String.valueOf(stackCount) : "");

                            posX += SLOT_WIDTH;
                            itemIndex++;
                        }

                        posX = event.x + (SLOT_WIDTH / 2);
                        posY += SLOT_WIDTH;
                    }

                    matrix3fStack.popMatrix();

                    return false;
                }
            }

            return true;
        });

        EventBus.register(AbstractContainerScreenTickEvent.class, event -> {
            if (event.abstractContainerScreen.getTitle().toString().contains("enderchest")) {
                echestWasOpened = true;
                enderChestItems.clear();
                event.abstractContainerScreen.getMenu().slots.forEach(slot -> {
                    if (enderChestItems.size() < 27)
                        enderChestItems.add(slot.getItem());
                });
            }

            return true;
        });
    }

}
