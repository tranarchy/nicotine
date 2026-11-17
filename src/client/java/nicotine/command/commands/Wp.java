package nicotine.command.commands;

import net.minecraft.ChatFormatting;
import nicotine.command.Command;
import nicotine.util.Message;
import nicotine.util.Player;
import nicotine.util.waypoint.Waypoint;
import nicotine.util.waypoint.WaypointManager;

import static nicotine.util.Common.*;

public class Wp extends Command {
    public Wp() {
        super("wp", "Sets a waypoint (.wp <add/remove> <name> [x] [y] [z])");
    }

    @Override
    public void trigger(String[] splitCommand) {
        if (splitCommand.length == 1) {
            for (Waypoint waypoint : WaypointManager.waypoints) {
                if (waypoint.server.equals(currentServer.ip)) {
                    Message.send(String.format("%s%s -> %s[%d %d %d] [%s]",
                            ChatFormatting.DARK_PURPLE,
                            waypoint.name,
                            ChatFormatting.GRAY,
                            waypoint.x,
                            waypoint.y,
                            waypoint.z,
                            waypoint.getFormattedDimensionName()
                    ));
                }
            }
        } else if (splitCommand.length == 6) {
            if (splitCommand[1].equals("add")) {
                String waypoint = splitCommand[2];
                try {
                    int x = Integer.parseInt(splitCommand[3]);
                    int y = Integer.parseInt(splitCommand[4]);
                    int z = Integer.parseInt(splitCommand[5]);
                    WaypointManager.add(waypoint, Player.getDimension(), currentServer.ip, x, y, z);
                    Message.sendInfo("Waypoint added");
                } catch (NumberFormatException e) {
                    Message.sendWarning("Coordinate was not a number! (.help)");
                }
            } else {
                Message.sendWarning("Wrong syntax! (.help)");
            }
        } else if (splitCommand.length == 3) {
            String waypoint = splitCommand[2];
            if (splitCommand[1].equals("add")) {
                WaypointManager.add(waypoint);
                Message.sendInfo("Waypoint added");
            } else if (splitCommand[1].equals("remove")) {
                WaypointManager.remove(waypoint);
                Message.sendInfo("Waypoint removed");
            } else {
                Message.sendWarning("Wrong syntax! (.help)");
            }
        } else {
            Message.sendWarning("Wrong argument count! (.help)");
        }
    }
}
