package nicotine.command.commands;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.mods.render.Peek;
import nicotine.screens.ShulkerPeekScreen;
import nicotine.util.EventBus;
import nicotine.util.Message;

import java.util.List;

import static nicotine.util.Common.*;

public class PeekHand {
    private static boolean openScreen = false;

    public static void init() {
        Command mods = new Command("peek", "Peeks the shulker box / echest in your hand") {
            @Override
            public void trigger(String[] splitCommand) {
              if (Peek.shulkerBoxItems.contains(mc.player.getMainHandStack().getItem()) || mc.player.getMainHandStack().getItem() == Items.ENDER_CHEST) {
                  openScreen = true;
              } else {
                  Message.sendWarning("You are not holding a shulker box or ender chest!");
              }
            }
        };
        CommandManager.addCommand(mods);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (mc.currentScreen == null && openScreen) {
                openScreen = false;
                ItemStack mainHandStack = mc.player.getMainHandStack();

                List<ItemStack> peekItems;

                if (mainHandStack.getItem() == Items.ENDER_CHEST) {
                    if (Peek.echestWasOpened) {
                        peekItems = Peek.enderChestItems;
                    } else {
                        Message.sendWarning("You haven't opened your ender chest yet!");
                        return true;
                    }
                } else {
                    ContainerComponent shulkerContainer = mainHandStack.getComponents().get(DataComponentTypes.CONTAINER);
                    peekItems = shulkerContainer.stream().toList();
                }

                SimpleInventory peekInventory = new SimpleInventory(9 * 3);

                for (int i = 0; i < peekItems.size(); i++) {
                    peekInventory.setStack(i, peekItems.get(i));
                }
                mc.setScreen(new ShulkerPeekScreen(mainHandStack.getName(), peekInventory));
            }

            return true;
        });
    }
}
