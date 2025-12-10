package nicotine.command.commands;

import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.events.ClientLevelTickEvent;
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

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (mc.screen == null && openScreen) {
                openScreen = false;
                List<ItemStack> peekItems;

                if (Peek.echestWasOpened) {
                    peekItems = Peek.enderChestItems;
                } else {
                    Message.sendWarning("You haven't opened your ender chest yet!");
                    return true;
                }

                SimpleContainer peekInventory = new SimpleContainer(9 * 3);

                for (int i = 0; i < peekItems.size(); i++) {
                    peekInventory.setItem(i, peekItems.get(i));
                }
                mc.setScreen(new PeekScreen(Component.literal("Ender Chest"), peekInventory));
            }

            return true;
        });
    }
}
