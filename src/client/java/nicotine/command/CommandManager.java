package nicotine.command;

import nicotine.command.commands.*;
import nicotine.events.SendMessageEvent;
import nicotine.util.EventBus;
import nicotine.util.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandManager {
    public static final List<Command> commands = new ArrayList<>();
    public static String prefix = ".";

    public static void addCommand(Command command) {
        commands.add(command);
    }

    public static void init() {
        Help.init();
        Prefix.init();
        Mods.init();
        Set.init();
        Connect.init();
        Echest.init();
        Waypoint.init();
        Friend.init();

        if (System.getProperty("os.name").startsWith("Mac"))
            TouchBarCustom.init();

        EventBus.register(SendMessageEvent.class, event -> {
            if (!event.content.startsWith(prefix))
                return true;

            String commandString = event.content.substring(1).toLowerCase();
            String[] splitCommand = commandString.split(" ");

            Optional<Command> command = commands.stream().filter(x -> x.name.equals(splitCommand[0])).findFirst();

            if (command.isPresent()) {
                command.get().trigger(splitCommand);
            } else {
                Message.sendWarning("Bad command! (.help)");
            }

            return false;
        });
    }
}
