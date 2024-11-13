package nicotine.mods.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nicotine.events.DrawMouseoverTooltipEvent;
import nicotine.util.EventBus;

import java.util.Arrays;
import java.util.List;

import static nicotine.util.Modules.*;
import static nicotine.util.Common.*;
import static nicotine.util.Colors.*;

public class ShulkerPeek {

    private static final List<Item> shulkerBoxTypes = Arrays.asList(
            Items.SHULKER_BOX,
            Items.WHITE_SHULKER_BOX,
            Items.ORANGE_SHULKER_BOX,
            Items.MAGENTA_SHULKER_BOX,
            Items.LIGHT_BLUE_SHULKER_BOX,
            Items.YELLOW_SHULKER_BOX,
            Items.LIME_SHULKER_BOX,
            Items.PINK_SHULKER_BOX,
            Items.GRAY_SHULKER_BOX,
            Items.LIGHT_GRAY_SHULKER_BOX,
            Items.CYAN_SHULKER_BOX,
            Items.PURPLE_SHULKER_BOX,
            Items.BLUE_SHULKER_BOX,
            Items.BROWN_SHULKER_BOX,
            Items.GREEN_SHULKER_BOX,
            Items.RED_SHULKER_BOX,
            Items.BLACK_SHULKER_BOX
    );


    public static void init() {
        Mod shulkerPeek = new Mod();
        shulkerPeek.name = "ShulkerPeek";
        modules.get(Category.Render).add(shulkerPeek);

        EventBus.register(DrawMouseoverTooltipEvent.class, event -> {
            if (!shulkerPeek.enabled)
                return true;

            final int slotWidth = 18;

            if (event.focusedSlot != null) {
                ItemStack focusedStack = event.focusedSlot.getStack();

                if (shulkerBoxTypes.contains(focusedStack.getItem())) {

                    ContainerComponent shulkerContainer = focusedStack.getComponents().get(DataComponentTypes.CONTAINER);

                    int posX = event.x + (slotWidth / 2);
                    int posY = event.y;
                    int totalWidth = posX + (slotWidth * 9);

                    int itemIndex = 0;

                    TextRenderer textRenderer = mc.textRenderer;
                    List<ItemStack> shulkerItems = shulkerContainer.stream().toList();
                    ItemStack shulkerItem;

                    event.drawContext.getMatrices().push();
                    event.drawContext.getMatrices().translate(0.0F, 0.0F, 1000.0F);

                    event.drawContext.fill(posX, posY, totalWidth, posY + textRenderer.fontHeight + 2 + (slotWidth * 3), 0xFF000000);
                    event.drawContext.drawHorizontalLine(posX, totalWidth, posY, 0xFF5F44C4);
                    event.drawContext.drawVerticalLine(posX, posY, posY + textRenderer.fontHeight + 2, FOREGROUND_COLOR);
                    event.drawContext.drawVerticalLine(totalWidth, posY, posY + textRenderer.fontHeight + 2, FOREGROUND_COLOR);

                    int offset = textRenderer.getWidth(focusedStack.getName());
                    event.drawContext.drawText(textRenderer, focusedStack.getName(), totalWidth - offset, posY + 2, FOREGROUND_COLOR, true);

                    posY += textRenderer.fontHeight + 2;

                    for (int i = 0; i < 3; i++) {

                        event.drawContext.drawHorizontalLine(posX, totalWidth, posY, FOREGROUND_COLOR);

                        for (int j = 0; j < 9; j++) {

                            if (shulkerItems.size() > itemIndex)
                                shulkerItem = shulkerItems.get(itemIndex);
                            else
                                shulkerItem = ItemStack.EMPTY;

                            int stackCount = shulkerItem.getCount();

                            if (i == 0)
                                event.drawContext.drawVerticalLine(posX, posY, posY + (slotWidth * 3), FOREGROUND_COLOR);

                            event.drawContext.drawItem(shulkerItem, posX + 1, posY + 1);

                            if (stackCount > 1)
                                event.drawContext.drawStackOverlay(textRenderer, shulkerItem, posX, posY, String.valueOf(stackCount));

                            posX += slotWidth;
                            itemIndex++;
                        }

                        if (i == 0)
                            event.drawContext.drawVerticalLine(posX, posY, posY + (slotWidth * 3), FOREGROUND_COLOR);

                        posX = event.x + (slotWidth / 2);
                        posY += slotWidth;
                    }

                    event.drawContext.drawHorizontalLine(posX, totalWidth, posY, FOREGROUND_COLOR);

                    event.drawContext.getMatrices().pop();

                    return false;
                }
            }

            return true;
        });
    }

}
