package nicotine.command.commands;

import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.util.Message;

public class Help {
    public static void init() {
        Command mods = new Command("help", "Explains what commands do") {
            @Override
            public void trigger(String[] splitCommand) {
                for (Command command : CommandManager.commands.stream().skip(1).toList()) {
                    Message.command(command.name, command.description);
                }
            }
        };
        CommandManager.addCommand(mods);
    }
}
