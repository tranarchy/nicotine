package nicotine.command.commands;

import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import nicotine.command.Command;
import nicotine.util.Message;

import static nicotine.util.Common.mc;

public class Connect extends Command {

    public Connect() {
        super("connect", "Connect to another server (.connect <IP/address>)");
    }

    @Override
    public void trigger(String[] splitCommand) {
        if (splitCommand.length != 2) {
            Message.sendWarning("Wrong argument count! (.help)");
            return;
        }

        ConnectScreen.startConnecting(new TitleScreen(), mc, ServerAddress.parseString(splitCommand[1]), new ServerData("", splitCommand[1], ServerData.Type.OTHER), false, null);
    }
}
