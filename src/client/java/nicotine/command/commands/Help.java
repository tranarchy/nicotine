package nicotine.command.commands;

import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.util.Message;

public class Help extends Command {

    public Help() {
        super("help", "Explains what commands do");
    }

    @Override
    public void trigger(String[] splitCommand) {
        for (Command command : CommandManager.commands.stream().skip(1).toList()) {
            Message.command(command.name, command.description.replace('.', CommandManager.prefix.charAt(0)));
        }
    }
}
