package nicotine.command.commands;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.mods.render.Peek;
import nicotine.screens.PeekScreen;
import nicotine.util.EventBus;
import nicotine.util.Message;

import java.util.List;

import static nicotine.util.Common.*;

public class Echest {
    private static boolean openScreen = false;

    public static void init() {
        Command echest = new Command("echest", "Shows your ender chest if you have opened it before") {
            @Override
            public void trigger(String[] splitCommand) {
              if (mc.player == null)
                  return;

                openScreen = true;
            }
        };
        CommandManager.addCommand(echest);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (mc.currentScreen == null && openScreen) {
                openScreen = false;
                List<ItemStack> peekItems;

                if (Peek.echestWasOpened) {
                    peekItems = Peek.enderChestItems;
                } else {
                    Message.sendWarning("You haven't opened your ender chest yet!");
                    return true;
                }

                SimpleInventory peekInventory = new SimpleInventory(9 * 3);

                for (int i = 0; i < peekItems.size(); i++) {
                    peekInventory.setStack(i, peekItems.get(i));
                }
                mc.setScreen(new PeekScreen(Text.of("Ender Chest"), peekInventory));
            }

            return true;
        });
    }
}
