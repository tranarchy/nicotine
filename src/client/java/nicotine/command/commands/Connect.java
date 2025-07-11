package nicotine.command.commands;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.util.Message;

import static nicotine.util.Common.mc;

public class Connect {
    public static void init() {
        Command connect = new Command("connect", "Connect to another server (.connect <IP/address>)") {
            @Override
            public void trigger(String[] splitCommand) {
                if (splitCommand.length != 2) {
                    Message.sendWarning("Wrong argument count! (.help)");
                    return;
                }

                ConnectScreen.connect(new TitleScreen(), mc, ServerAddress.parse(splitCommand[1]), new ServerInfo("", splitCommand[1], ServerInfo.ServerType.OTHER), false, null);
            }
        };
        CommandManager.addCommand(connect);
    }
}
