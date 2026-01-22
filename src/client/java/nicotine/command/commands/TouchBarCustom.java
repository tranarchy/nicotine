package nicotine.command.commands;

import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.mod.Mod;
import nicotine.mod.ModManager;
import nicotine.mod.mods.misc.TouchBar;
import nicotine.util.Message;
import nicotine.util.Settings;

import java.util.*;

public class TouchBarCustom extends Command {

    final public static LinkedHashMap<String, String> customTouchBarItems = new LinkedHashMap<>();

    public TouchBarCustom() {
        super("touchbar", "Add custom items to the touch bar (.touchbar <add/remove> <mod/command> <btn name> <mod name / command args>)");
    }

    @Override
    public void trigger(String[] splitCommand) {
        if (splitCommand.length < 3) {
            Message.sendWarning("Wrong argument count! (.help)");
            return;
        }

        String operation = splitCommand[1];

        if (operation.equals("add")) {
            if (splitCommand.length < 5) {
                Message.sendWarning("Wrong argument count! (.help)");
                return;
            }

            String type = splitCommand[2];
            String btnName = splitCommand[3];
            String name = splitCommand[4];

            if (type.equalsIgnoreCase("mod")) {
                Mod mod = ModManager.getMod(name);
                if (mod == null) {
                    Message.sendWarning("Invalid mod!");
                    return;
                }

                customTouchBarItems.put(btnName, mod.name);
            } else if (type.equalsIgnoreCase("command")) {
                boolean validCommand = false;

                for (Command command : CommandManager.commands) {
                    if (command.name.equalsIgnoreCase(name)) {
                        validCommand = true;
                        break;
                    }
                }

                if (!validCommand) {
                    Message.sendWarning("Invalid command!");
                    return;
                }

                StringBuilder command = new StringBuilder();

                for (int i = 4; i < splitCommand.length; i++) {
                    command.append(splitCommand[i]).append(" ");
                }

                command.deleteCharAt(command.length() - 1);

                customTouchBarItems.put(btnName, command.toString());
            }

            Message.send("Added new item to the touch bar");
            TouchBar.setTouchBar();
            Settings.save();
        } else if (operation.equals("remove")) {
            String btnName = splitCommand[2];

            if (!customTouchBarItems.containsKey(btnName)) {
                Message.sendWarning("Touch bar item doesn't exist!");
                return;
            }

            customTouchBarItems.remove(btnName);
            for (TouchBar.TouchBarButton touchBarButton : TouchBar.buttons.stream().toList()) {
                if (touchBarButton.identifier.equals(btnName)) {
                    TouchBar.buttons.remove(touchBarButton);
                    break;
                }
            }

            Message.send("Removed touch bar item");
            TouchBar.setTouchBar();
            Settings.save();
        } else {
            Message.sendWarning("Invalid syntax!");
        }
    }
}
