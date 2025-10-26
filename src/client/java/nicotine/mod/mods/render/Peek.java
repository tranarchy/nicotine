package nicotine.mod.mods.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.DrawMouseoverTooltipEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.PeekScreen;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Keybind;
import nicotine.util.render.GUI;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class Peek {
    public static boolean echestWasOpened = false;
    public static List<ItemStack> enderChestItems = new ArrayList<>();

    public static void init() {
        Mod peek = new Mod("Peek", "Lets you see inside shulkers and echets without opening them");
        ToggleOption shulker = new ToggleOption("Shulker", true);
        ToggleOption enderChest = new ToggleOption("EnderChest");
        KeybindOption keybindOption = new KeybindOption("InspectKey", InputUtil.GLFW_KEY_LEFT_ALT);
        peek.modOptions.addAll(Arrays.asList(shulker, enderChest, keybindOption));
        ModManager.addMod(ModCategory.Render, peek);

        final int SLOT_WIDTH = 18;
        final int MAX_WIDTH = SLOT_WIDTH * 9;

        EventBus.register(DrawMouseoverTooltipEvent.class, event -> {
            if (!peek.enabled)
                return true;

            if (event.focusedSlot != null) {
                ItemStack focusedStack = event.focusedSlot.getStack();

                if ((focusedStack.isIn(ItemTags.SHULKER_BOXES) && shulker.enabled) || (focusedStack.getItem() == Items.ENDER_CHEST && enderChest.enabled) && echestWasOpened) {

                    List<ItemStack> itemsToPeek;

                    if (focusedStack.getItem() == Items.ENDER_CHEST) {
                        itemsToPeek = enderChestItems;
                    } else {
                        ContainerComponent shulkerContainer = focusedStack.getComponents().get(DataComponentTypes.CONTAINER);
                        itemsToPeek = shulkerContainer.stream().toList();
                    }

                    if (InputUtil.isKeyPressed(mc.getWindow(), keybindOption.keyCode)) {
                        SimpleInventory peekInventory = new SimpleInventory(9 * 3);

                        for (int i = 0; i < itemsToPeek.size(); i++) {
                            peekInventory.setStack(i, itemsToPeek.get(i));
                        }
                        mc.setScreen(new PeekScreen(focusedStack.getName(), peekInventory));
                    }

                    int posX = event.x + (SLOT_WIDTH / 2);
                    int posY = event.y;

                    int itemIndex = 0;

                    TextRenderer textRenderer = mc.textRenderer;

                    ItemStack shulkerItem;

                    Matrix3x2fStack matrix3fStack = event.drawContext.getMatrices().pushMatrix();

                    event.drawContext.getMatrices().translate(0.0F, 0.0F, matrix3fStack.identity());

                    event.drawContext.fill(posX, posY, posX + MAX_WIDTH, posY + textRenderer.fontHeight + 4, ColorUtil.BACKGROUND_COLOR);
                    GUI.drawBorder(event.drawContext, posX, posY, MAX_WIDTH, textRenderer.fontHeight + 4, ColorUtil.BORDER_COLOR);
                    event.drawContext.drawText(textRenderer, focusedStack.getName(), posX + 3, posY + 3, ColorUtil.FOREGROUND_COLOR, true);

                    posY += textRenderer.fontHeight + 4;

                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 9; j++) {

                            shulkerItem = itemsToPeek.size() > itemIndex ? itemsToPeek.get(itemIndex) : ItemStack.EMPTY;
                            int stackCount = shulkerItem.getCount();

                            event.drawContext.fill(posX, posY, posX + SLOT_WIDTH, posY + SLOT_WIDTH, ColorUtil.BACKGROUND_COLOR);
                            GUI.drawBorder(event.drawContext, posX, posY, SLOT_WIDTH, SLOT_WIDTH, ColorUtil.BORDER_COLOR);

                            event.drawContext.drawItem(shulkerItem, posX + 1, posY + 1);
                            event.drawContext.drawStackOverlay(textRenderer, shulkerItem, posX + 1, posY + 1, stackCount > 1 ? String.valueOf(stackCount) : "");

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

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (mc.currentScreen instanceof GenericContainerScreen genericContainerScreen) {
                if (genericContainerScreen.getTitle().toString().contains("enderchest")) {
                    echestWasOpened = true;
                    enderChestItems.clear();
                    genericContainerScreen.getScreenHandler().slots.forEach(slot -> {
                        if (enderChestItems.size() < 27)
                            enderChestItems.add(slot.getStack());
                    });
                }
            }

            return true;
        });
    }

}
