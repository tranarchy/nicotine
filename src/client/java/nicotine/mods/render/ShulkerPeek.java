package nicotine.mods.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nicotine.events.DrawMouseoverTooltipEvent;
import nicotine.util.EventBus;
import nicotine.util.RenderGUI;

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

        final int SLOT_WIDTH = 18;
        final int MAX_WIDTH = SLOT_WIDTH * 9;

        EventBus.register(DrawMouseoverTooltipEvent.class, event -> {
            if (!shulkerPeek.enabled)
                return true;

            mc.player.openRidingInventory();

            if (event.focusedSlot != null) {
                ItemStack focusedStack = event.focusedSlot.getStack();

                if (shulkerBoxTypes.contains(focusedStack.getItem())) {

                    ContainerComponent shulkerContainer = focusedStack.getComponents().get(DataComponentTypes.CONTAINER);

                    int posX = event.x + (SLOT_WIDTH / 2);
                    int posY = event.y;

                    int itemIndex = 0;

                    TextRenderer textRenderer = mc.textRenderer;
                    List<ItemStack> shulkerItems = shulkerContainer.stream().toList();
                    ItemStack shulkerItem;

                    event.drawContext.getMatrices().push();
                    event.drawContext.getMatrices().translate(0.0F, 0.0F, 1000.0F);

                    event.drawContext.fill(posX, posY, posX + MAX_WIDTH, posY + textRenderer.fontHeight + 4, BACKGROUND_COLOR);
                    RenderGUI.drawBorder(event.drawContext, posX, posY, MAX_WIDTH, textRenderer.fontHeight + 4, FOREGROUND_COLOR);
                    event.drawContext.drawText(textRenderer, focusedStack.getName(), posX + 3, posY + 3, FOREGROUND_COLOR, true);

                    posY += textRenderer.fontHeight + 4;

                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 9; j++) {

                            shulkerItem = shulkerItems.size() > itemIndex ? shulkerItems.get(itemIndex) : ItemStack.EMPTY;
                            int stackCount = shulkerItem.getCount();

                            event.drawContext.fill(posX, posY, posX + SLOT_WIDTH, posY + SLOT_WIDTH, BACKGROUND_COLOR);
                            RenderGUI.drawBorder(event.drawContext, posX, posY, SLOT_WIDTH, SLOT_WIDTH, FOREGROUND_COLOR);

                            event.drawContext.drawItem(shulkerItem, posX + 1, posY + 1);
                            if (stackCount > 1)
                                event.drawContext.drawStackOverlay(textRenderer, shulkerItem, posX + 1, posY + 1, String.valueOf(stackCount));

                            posX += SLOT_WIDTH;
                            itemIndex++;
                        }

                        posX = event.x + (SLOT_WIDTH / 2);
                        posY += SLOT_WIDTH;
                    }

                    event.drawContext.getMatrices().pop();

                    return false;
                }
            }

            return true;
        });
    }

}
