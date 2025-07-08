package nicotine.command.commands;

import net.fabricmc.loader.api.FabricLoader;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.util.Message;
import nicotine.util.Settings;

public class Prefix {
    public static void init() {
        Command prefix = new Command("prefix", "Changes the command prefix (.prefix <prefix>)") {
            @Override
            public void trigger(String[] splitCommand) {
                if (splitCommand.length != 2) {
                    Message.sendWarning("Wrong argument count! (.help)");
                    return;
                }

                CommandManager.prefix = splitCommand[1].substring(0, 1);
                Message.send(String.format("Set command prefix to %s", CommandManager.prefix));
                Settings.save();
            }
        };
        CommandManager.addCommand(prefix);
    }
}
