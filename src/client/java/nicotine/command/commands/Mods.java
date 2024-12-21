package nicotine.command.commands;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.util.Message;

public class Mods {
    public static void init() {
        Command mods = new Command("mods", "Lists all loaded Fabric mods") {
            @Override
            public void trigger(String[] splitCommand) {
                Message.send(String.format("%s (%d)", FabricLoader.getInstance().getAllMods().toString(), FabricLoader.getInstance().getAllMods().size()));
            }
        };
        CommandManager.addCommand(mods);
    }
}
